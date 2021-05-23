package com.dkmarkell.redditfeed.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FeedItemDao {

    @Insert
    fun insertFeedItem(item: FeedItemEntity)

    @Query("DELETE FROM pinned_feed_item WHERE name_id = :nameId")
    fun deleteFeedItem(nameId: String)

    @Query("SELECT * FROM pinned_feed_item ORDER BY created DESC")
    fun getFeedItems(): List<FeedItemEntity>

    @Query("SELECT * FROM pinned_feed_item ORDER BY created DESC")
    fun observeFeedItems(): LiveData<List<FeedItemEntity>>

    @Query("SELECT EXISTS (SELECT * FROM pinned_feed_item WHERE name_id = :nameId)")
    fun feedItemExists(nameId: String): Boolean

}