package com.fatihparkin.snapnews

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatihparkin.snapnews.databinding.ActivityMainBinding
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//Data classes

data class NewsResponse(
    @SerializedName("articles") val articles: List<Article>
)

data class Article(
    @SerializedName("source") val source: Source,
    @SerializedName("title") val title: String,
    @SerializedName("urlToImage") val imageUrl: String?,
    @SerializedName("publishedAt") val publishedAt: String,
)

data class Source(
    @SerializedName("name") val name: String
)

//APİ
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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        fetchNews()
    }

    private fun fetchNews() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getNews()
                if (response.isSuccessful) {
                    val newsList = response.body()?.articles ?: emptyList()
                    runOnUiThread {
                        //Recycler view
                        binding.recyclerView.adapter = NewsAdapter(newsList)
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Hata: ${e.message}")
            }
        }
    }
}
