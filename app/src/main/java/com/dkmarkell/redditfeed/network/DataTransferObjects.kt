package com.dkmarkell.redditfeed.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

class RedditNewsResponse(val data: RedditDataResponse)

class RedditDataResponse(
    val children: List<RedditChildrenResponse>,
    val after: String?,
    val before: String?
)

class RedditChildrenResponse(val data: RedditFeedItem)

@Parcelize
data class RedditFeedItem(
    @Json(name = "name")val nameId: String,
    val title: String,
    val created: Long,
    val thumbnail: String,
    val url: String,
    @Json(name = "subreddit_name_prefixed")val subreddit: String,
    val author: String
) : Parcelable