package com.example.wheelify.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wheelify.data.response.ArticlesItem
import com.example.wheelify.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _news = MutableLiveData<ArticlesItem?>()
    val news: LiveData<ArticlesItem?> = _news

    fun getDetailNews(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getDetailNews(id)
                _news.value = response.articles?.firstOrNull()
            } catch (e: Exception) {
                _news.value = null
                Log.e("DetailViewModel", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


}