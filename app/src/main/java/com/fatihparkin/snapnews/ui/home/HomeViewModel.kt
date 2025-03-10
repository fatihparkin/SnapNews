package com.fatihparkin.snapnews.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fatihparkin.snapnews.data.model.NewsResponse
import com.fatihparkin.snapnews.data.repository.NewsRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: NewsRepository) : ViewModel() {

    val newsLiveData: LiveData<NewsResponse> get() = repository.newsLiveData

    fun fetchNews(query: String? = null) {
        viewModelScope.launch {
            repository.fetchNews(query = query, apiKey = "2ac631b9cae3401dbe27515140ca5150")
        }
    }
}

class HomeViewModelFactory(private val repository: NewsRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
