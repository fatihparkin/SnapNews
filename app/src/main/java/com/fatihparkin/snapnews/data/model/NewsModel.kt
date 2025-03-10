package com.fatihparkin.snapnews.data.model

import com.fatihparkin.snapnews.data.remote.NewsApi
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// API yanıt modeli
data class NewsResponse(
    @SerializedName("articles") val articles: List<Article>
)

// Haber makalesi modeli
data class Article(
    @SerializedName("source") val source: Source,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val imageUrl: String?,
    @SerializedName("publishedAt") val publishedAt: String
)

// Haber kaynağı modeli
data class Source(
    @SerializedName("name") val name: String
)

// Retrofit API istemcisi
object RetrofitClient {
    private const val BASE_URL = "https://newsapi.org/v2/"

    val api: NewsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }
}
