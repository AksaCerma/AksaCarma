package com.example.aksacarma.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
	@field:SerializedName("HistoryResponse")
	val historyResponse: List<HistoryResponseItem>
)

data class HistoryResponseItem(

	@field:SerializedName("datetime")
	val datetime: String,

	@field:SerializedName("google_result")
	val googleResult: List<GoogleResultItem>,

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("prediction")
	val prediction: String
)
