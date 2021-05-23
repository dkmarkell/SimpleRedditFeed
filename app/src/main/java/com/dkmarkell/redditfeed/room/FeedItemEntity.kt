package com.dkmarkell.redditfeed.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dkmarkell.redditfeed.domain.FeedItem

@Entity(tableName = "pinned_feed_item")
data class FeedItemEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "feed_item_id") val feedItemId: Long = 0L,
    @ColumnInfo(name = "name_id")var nameId: String,
    val title: String,
    val created: Long,
    val thumbnail: String,
    val url: String,
    val subreddit: String,
    val author: String
)

/**
 * Converts a FeedItem to a FeedItemEntity
 */
fun FeedItem.toEntity() = FeedItemEntity(
    nameId = nameId,
    title = title,
    created = created,
    thumbnail = thumbnail,
    url = url,
    subreddit = subreddit,
    author = author
)