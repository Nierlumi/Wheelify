package com.example.wheelify.data.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(

	@field:SerializedName("articles")
	val articles: List<ArticlesItem> = listOf(),

	@field:SerializedName("status")
	val status: String? = null
)

data class ArticlesItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
