package com.dkmarkell.redditfeed.domain

import android.os.Parcelable
import com.dkmarkell.redditfeed.network.RedditFeedItem
import com.dkmarkell.redditfeed.room.FeedItemEntity
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class FeedItem(
    val title: String,
    val nameId: String,
    val created: Long,
    val thumbnail: String,
    val url: String,
    val subreddit: String,
    val dateFormatted: String,
    val author: String,
    var pinned: Boolean
) : Parcelable

/**
 * Converts a feedItemEntity to a FeedItem
 */
fun FeedItemEntity.toModel(): FeedItem {
    val dateString =
        SimpleDateFormat("MM/dd/yyyy", Locale.CANADA).format(Date(created * 1000L))

    return FeedItem (
        title = title,
        nameId = nameId,
        created = created,
        thumbnail = thumbnail,
        url = url,
        subreddit = subreddit,
        dateFormatted = dateString,
        author = author,
        pinned = true
    )
}

/**
 * Converts a RedditFeedItem to a FeedItem
 */
fun RedditFeedItem.toModel(pinned: Boolean): FeedItem {
    val dateString =
        SimpleDateFormat("MM/dd/yyyy", Locale.CANADA).format(Date(created * 1000L))

    return FeedItem(
        title = title,
        nameId = nameId,
        created = created,
        thumbnail = thumbnail,
        url = url,
        subreddit = subreddit,
        dateFormatted = dateString,
        author = author,
        pinned = pinned
    )
}