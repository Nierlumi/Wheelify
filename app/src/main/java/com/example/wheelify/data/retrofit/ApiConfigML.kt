package com.example.wheelify.data.retrofit

import com.yalantis.ucrop.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfigML {
    fun getApiServiceML(): ApiServiceML {
        val loggingInterceptor = if(BuildConfig.DEBUG)
        { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) }else
        { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)}
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://wheelify-model-api-2-199228555553.asia-southeast2.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiServiceML::class.java)
    }
}