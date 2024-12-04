package com.example.wheelify.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wheelify.data.response.ArticlesItem
import com.example.wheelify.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

   private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _news = MutableLiveData<List<ArticlesItem>>()
    val news : LiveData<List<ArticlesItem>> = _news

    fun getNews(country: String, category: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getNews(country, category + "&timestamp=${System.currentTimeMillis()}" )
                _news.value = response.articles ?: emptyList()
            } catch (e: Exception){
                _news.value = emptyList()
                Log.e("HomeViewModel", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }

    }
}