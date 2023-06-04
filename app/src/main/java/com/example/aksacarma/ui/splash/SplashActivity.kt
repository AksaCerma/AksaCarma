package com.example.aksacarma.ui.splash

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aksacarma.R
import com.example.aksacarma.ui.login.LoginActivity
import com.example.aksacarma.ui.main.MainActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        GlobalScope.launch {
            delay(VALUE_SPLASH)
            LoginActivity.start(this@SplashActivity)
            finish()
        }
    }

    companion object {
        const val VALUE_SPLASH = 1000L
    }
}