package com.fatihparkin.snapnews.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fatihparkin.snapnews.data.model.NewsResponse
import com.fatihparkin.snapnews.data.remote.NewsApi
import retrofit2.Response

class NewsRepository(private val newsApi: NewsApi) {

    private val _newsLiveData = MutableLiveData<NewsResponse>()
    val newsLiveData: LiveData<NewsResponse> get() = _newsLiveData

    suspend fun fetchNews(country: String = "us", query: String? = null, apiKey: String) {
        try {
            val response: Response<NewsResponse> = newsApi.getNews(country, query, apiKey)
            Log.d("NewsRepository", "API çağrısı yapıldı: ${response.raw()}")

            if (response.isSuccessful && response.body() != null) {
                Log.d("NewsRepository", "Başarılı Yanıt: ${response.body()}")
                _newsLiveData.postValue(response.body())
            } else {
                Log.e("NewsRepository", "API Hatası: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("NewsRepository", "API Çağrısı Başarısız: ${e.message}")
        }
    }
}
