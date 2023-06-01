package com.example.aksacarma.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictionResult(

	@field:SerializedName("google_result")
	val googleResult: List<GoogleResultItem>,

	@field:SerializedName("prediction")
	val prediction: String
)