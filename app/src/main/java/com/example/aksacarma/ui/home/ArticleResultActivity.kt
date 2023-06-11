package com.example.aksacarma.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aksacarma.databinding.ActivityArticleResultBinding
import com.example.aksacarma.ui.main.MainActivity

class ArticleResultActivity : AppCompatActivity() {

    private lateinit var binding : ActivityArticleResultBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        binding.apply {
            val url = intent.getStringExtra("url")
            webView.loadUrl("$url")
            webView.settings.javaScriptEnabled = true
        }
    }

    private fun setupView() {
        binding.toolbarGoogleResult.imageViewBack.setOnClickListener{
            val intent = Intent(this@ArticleResultActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}