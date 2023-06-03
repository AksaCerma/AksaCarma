package com.example.aksacarma.ui.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.aksacarma.data.remote.response.PredictionResult
import com.example.aksacarma.databinding.ActivityResultBinding
import com.example.aksacarma.ui.ViewModelFactory

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var factory: ViewModelFactory
    private val resultViewModel: ResultViewModel by viewModels { factory}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupUser()
    }


    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupUser() {
        resultViewModel.predictionResult.observe(this) {
          getPredictionResult(it)
        }
    }

    private fun getPredictionResult(predictionResult: PredictionResult) {
        binding.apply {
          textViewResult.text =  predictionResult.prediction
        }
    }

}