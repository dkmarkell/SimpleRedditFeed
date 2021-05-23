package com.dkmarkell.redditfeed.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .baseUrl("https://www.reddit.com")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface  RedditApiService {
    @GET("/hot.json")
    suspend fun getHot(@Query("after") after: String,
                       @Query("limit") limit: String)
            : RedditNewsResponse
}

object RedditApi {
    val retrofitService: RedditApiService by lazy {
        retrofit.create(RedditApiService::class.java)
    }
}