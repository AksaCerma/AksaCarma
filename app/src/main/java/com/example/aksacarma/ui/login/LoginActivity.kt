package com.example.aksacarma.ui.login

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
import com.example.aksacarma.databinding.ActivityLoginBinding
import com.example.aksacarma.model.UserModel
import com.example.aksacarma.ui.ViewModelFactory
import com.example.aksacarma.ui.main.MainActivity
import com.example.aksacarma.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels{factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setupView()
        setupViewModel()
        setupAction()

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupAction() {
       binding.apply {
           buttonLogin.setOnClickListener {
               val email = inputTextUsername.text.toString()
               val password = inputTextPassword.text.toString()

               if (email.isEmpty() && password.isEmpty()) {
                   inputTextUsername.error = getString(R.string.error_textField)
                   inputTextPassword.setError(getString(R.string.error_textField), null)
               } else if (email.isNotEmpty() && password.isNotEmpty()) {
                   showLoading()
                   postText()
                   showToast()
                   loginViewModel.getToken()
                   moveActivity()
               }
           }

           textviewRegister.setOnClickListener {
               val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
               startActivity(intent)
           }
       }

    }

    private fun postText() {
        binding.apply {
           loginViewModel.loginUser(
                username = inputTextUsername.text.toString(),
                password = inputTextPassword.text.toString()
            )
        }

        loginViewModel.loginResponse.observe(this@LoginActivity) {response ->
            getLoginUser(
                UserModel(
                    response.loginResult?.name.toString(),
                    AUTH_KEY + (response.loginResult?.token.toString()),
                    true
                )
            )
        }
    }

    private fun getLoginUser(user: UserModel) {
        loginViewModel.getLoginUser(user)
    }

    private fun moveActivity() {
        loginViewModel.loginResponse.observe(this@LoginActivity) {response ->
            if (!response.error) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun showToast() {
        loginViewModel.textToast.observe(this) {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading() {
        loginViewModel.isLoading.observe(this@LoginActivity) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    companion object {
        private const val AUTH_KEY = "Bearer "
    }
}