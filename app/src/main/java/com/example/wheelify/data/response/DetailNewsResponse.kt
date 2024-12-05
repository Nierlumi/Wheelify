package com.example.wheelify.data.response

import com.google.gson.annotations.SerializedName

data class DetailNewsResponse(

	@field:SerializedName("article")
	val article: Article? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Article(

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
