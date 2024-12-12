package com.example.wheelify.ui.scan

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wheelify.data.response.FileUploadResponse
import com.example.wheelify.data.retrofit.ApiConfigML
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ScanViewModel : ViewModel() {

    private val _scanResult = MutableLiveData<FileUploadResponse>()
    val scanResult: LiveData<FileUploadResponse> = _scanResult
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var currentImageUri: Uri? = null

    fun uploadImage(multipartBody: MultipartBody.Part) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfigML.getApiServiceML().uploadImage(multipartBody)
                _scanResult.value = response

            } catch (e: Exception) {
                _isLoading.value = false
                Log.e("ScanViewModel", "Error: ${e.message}")
            }
        }

    }
}