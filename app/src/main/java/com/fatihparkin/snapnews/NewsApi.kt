package com.fatihparkin.snapnews

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String = "android",
        @Query("apiKey") apiKey: String = "2ac631b9cae3401dbe27515140ca5150"
    ): Response<NewsResponse>
}
