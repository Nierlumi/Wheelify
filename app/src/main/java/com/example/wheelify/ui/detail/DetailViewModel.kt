package com.example.wheelify.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wheelify.data.response.DetailNewsResponse
import com.example.wheelify.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _news = MutableLiveData<DetailNewsResponse>()
    val news: LiveData<DetailNewsResponse> = _news

    fun getDetailNews(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getDetailNews(id)
                _news.value = response
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


}