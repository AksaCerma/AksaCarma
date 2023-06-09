package com.example.aksacarma.ui.result

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aksacarma.databinding.ActivityGoogleResultBinding

class GoogleResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoogleResultBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val url = intent.getStringExtra("url")
            webView.loadUrl("$url")
            webView.settings.javaScriptEnabled = true
        }
    }
}