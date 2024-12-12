package com.example.wheelify.data.retrofit

import com.example.wheelify.data.response.FileUploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServiceML {

    @Multipart
    @POST("process")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): FileUploadResponse

}