package com.example.aksacarma.ui.result

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import com.example.aksacarma.databinding.ActivityGoogleResultBinding
import com.example.aksacarma.ui.ViewModelFactory
import com.example.aksacarma.ui.camera.CameraViewModel

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