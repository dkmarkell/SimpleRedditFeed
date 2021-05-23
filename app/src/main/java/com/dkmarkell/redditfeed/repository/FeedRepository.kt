package com.dkmarkell.redditfeed.repository


import com.dkmarkell.redditfeed.domain.FeedItem
import com.dkmarkell.redditfeed.domain.toModel
import com.dkmarkell.redditfeed.network.RedditApi
import com.dkmarkell.redditfeed.network.RedditFeedItem
import com.dkmarkell.redditfeed.room.FeedItemDatabase
import com.dkmarkell.redditfeed.room.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class FeedRepository(database: FeedItemDatabase) {

    private val feedItemDao = database.databaseDao

    val pinnedList = feedItemDao.observeFeedItems()

    /**
     * suspend function that runs on the IO dispatcher to get the main feed
     *
     * Returned list is combined from the reddit's hot feed and user pinned items
     */
    suspend fun getFeed(): List<FeedItem> = withContext(Dispatchers.IO) {
        try {
            /**
             * get 25 items from reddit and map the RedditChildrenResponse objects to
             * FeedItem objects
             */
            val remoteList = RedditApi.retrofitService.getHot("", "25").data.children.map {
                val item = it.data

                /**
                 * check if the item from reddit is already pinned (saved in local db)
                 */
                val pinned = feedItemDao.feedItemExists(item.nameId)

                /**
                 * Create RedditFeedItem and convert it to a FeedItem
                 */
                RedditFeedItem(
                    item.nameId, item.title, item.created,
                    item.thumbnail, item.url, item.subreddit,
                    item.author
                ).toModel(pinned)
            }

            /**
             * Get the list of pinned items from Room
             */
            val localList = feedItemDao.getFeedItems().map { it.toModel() }

            /**
             * Combine the list of from reddit and the list from room
             *
             * Only keep one item if an item appears in both lists
             */
            val combinedList = (remoteList + localList).distinctBy { it.nameId }

            combinedList
        } catch (e: Exception) {
            /**
             * Catch any errors from the retrofit request.
             *
             * If this was production code, error handling would be beefed up and an
             * actual response returned to the caller
             */
            feedItemDao.getFeedItems().map { it.toModel() }
        }
    }

    /**
     * Suspend function that adds a pinned item to the database (Room)
     */
    suspend fun addPinnedItem(item: FeedItem) = withContext(Dispatchers.IO) {
        feedItemDao.insertFeedItem(item.toEntity())
    }

    /**
     * Suspend function that deletes an item from the database (room)
     */
    suspend fun deletePinnedItem(item: FeedItem) = withContext(Dispatchers.IO) {
        feedItemDao.deleteFeedItem(item.nameId)
    }
}