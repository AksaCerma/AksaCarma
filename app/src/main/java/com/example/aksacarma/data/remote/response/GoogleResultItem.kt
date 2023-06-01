package com.example.aksacarma.data.remote.response

import com.google.gson.annotations.SerializedName

data class GoogleResultItem(

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("url")
	val url: String
)