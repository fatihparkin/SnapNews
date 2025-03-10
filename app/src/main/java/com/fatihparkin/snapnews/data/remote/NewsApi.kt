package com.fatihparkin.snapnews.data.remote

import com.fatihparkin.snapnews.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country: String = "us",
        @Query("q") query: String? = null,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}
