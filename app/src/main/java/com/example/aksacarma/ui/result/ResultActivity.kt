package com.example.aksacarma.ui.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aksacarma.data.remote.response.PredictionResult
import com.example.aksacarma.databinding.ActivityResultBinding
import com.example.aksacarma.ui.ViewModelFactory
import com.example.aksacarma.ui.camera.CameraViewModel

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var factory: ViewModelFactory
    private val resultViewModel: CameraViewModel by viewModels { factory}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupUser()

        binding.recyclerViewResult.adapter = ListGoogleResultAdapter(emptyList())
        showRecyclerView()
    }


    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupUser() {
        resultViewModel.predictionResponse.observe(this) { predictionResponse ->
            val predictionResult = predictionResponse.predictionResult
            getPredictionResult(predictionResult)
        }
    }

    private fun getPredictionResult(predictionResult: PredictionResult) {
        binding.apply {
            binding.textViewResult.text = predictionResult.prediction
            binding.recyclerViewResult.adapter = ListGoogleResultAdapter(predictionResult.googleResult)

        }
    }

    private fun showRecyclerView() {
        binding.recyclerViewResult.apply {
            layoutManager = LinearLayoutManager(this@ResultActivity)
            setHasFixedSize(true)
        }
    }

}