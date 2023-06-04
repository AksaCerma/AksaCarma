package com.example.aksacarma.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        setupViewModel()
        setupAction()

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

        loginViewModel.loginResponse.observe(this@LoginActivity) {
            getLoginUser(
                UserModel(
                    it.loginResult?.username.toString(),
                    it.loginResult?.name.toString(),
                    "",
                    it.loginResult?.token.toString(),
                    true
                )
            )
        }
    }

    private fun getLoginUser(user: UserModel) {
        loginViewModel.getLoginUser(user)
    }

    private fun moveActivity() {
        loginViewModel.loginResponse.observe(this@LoginActivity) {
            if (!it.error) {
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
        fun start(context: Context) {
            Intent(context, MainActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }
}