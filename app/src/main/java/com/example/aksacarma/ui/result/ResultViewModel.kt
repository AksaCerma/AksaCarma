package com.example.aksacarma.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import com.example.aksacarma.data.remote.response.PredictionResult
import com.example.aksacarma.repository.UserRepository

class ResultViewModel (private val repository: UserRepository) : ViewModel() {
    val predictionResult: LiveData<PredictionResult> = repository.predictionResult
}