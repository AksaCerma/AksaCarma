package com.example.aksacarma.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

	@field:SerializedName("prediction_result")
	val predictionResult: PredictionResult,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)