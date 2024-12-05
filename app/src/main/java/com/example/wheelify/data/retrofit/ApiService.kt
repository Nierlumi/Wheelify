package com.example.wheelify.data.retrofit

import com.example.wheelify.data.response.DetailNewsResponse
import com.example.wheelify.data.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("news")
    suspend fun getNews(
        //@Query("apiKey") apiKey: String,
        @Query("country=us") country: String,
        @Query("category=technology") category: String
    ): NewsResponse

    @GET("news/{id}")
    suspend fun getDetailNews(
        @Path("id") id: Int
    ): DetailNewsResponse

}