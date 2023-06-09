package com.example.aksacarma.ui.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.example.aksacarma.R
import com.example.aksacarma.databinding.ActivityRegisterBinding
import com.example.aksacarma.ui.ViewModelFactory
import com.example.aksacarma.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var factory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupAction() {
        binding.apply {
            buttonLogin.setOnClickListener {
                val name = inputTextName.text.toString()
                val email = inputTextUsername.text.toString()
                val password = inputTextPassword.text.toString()

                if (name.isEmpty() && email.isEmpty() && password.isEmpty()) {
                    inputTextName.error = getString(R.string.error_textField)
                    inputTextUsername.error = getString(R.string.error_textField)
                    inputTextPassword.setError(getString(R.string.error_textField), null)
                } else if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    showLoading()
                    postText()
                    showToast()
                    moveActivity()
                }
            }

            textViewLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))

            }
        }
    }
    private fun moveActivity() {
        registerViewModel.registerResponse.observe(this@RegisterActivity) {response ->
            if (!response.error) {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun postText() {
        val avatar = "https://cdn.jeyy.xyz/image/avatar_url_9f3ef1.png"
        binding.apply {
            registerViewModel.registerUser(
                username = inputTextUsername.text.toString(),
                password = inputTextPassword.text.toString(),
                name = inputTextName.text.toString(),
                avatar = avatar
            )
        }
    }

    private fun showToast() {
        registerViewModel.textToast.observe(this) {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading() {
        registerViewModel.isLoading.observe(this@RegisterActivity) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}

