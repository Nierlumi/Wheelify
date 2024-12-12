package com.example.wheelify.data.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("confidence")
	val confidence: Double? = null,

	@field:SerializedName("class")
	val jsonMemberClass: String? = null
)
