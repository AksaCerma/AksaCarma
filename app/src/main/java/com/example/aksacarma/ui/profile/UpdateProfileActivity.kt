package com.example.aksacarma.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.aksacarma.databinding.ActivityUpdateProfileBinding
import com.example.aksacarma.ui.ViewModelFactory
class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUpdateProfileBinding
    private lateinit var factory: ViewModelFactory
    private val profileViewModel: ProfileViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupUser()
        updateUser()
        setupView()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupView() {
        binding.toolbarUpdateProfile.imageViewBack.setOnClickListener {
          finish()
        }
    }

    private fun setupUser() {
        profileViewModel.getUser().observe(this@UpdateProfileActivity) {
            setUserData(it.token)
        }
    }

    private fun setUserData(token: String) {
        profileViewModel.getUserData(token)
    }

    private fun updateUser() {
        binding.apply {
            buttonSave.setOnClickListener {
                profileViewModel.getUser().observe(this@UpdateProfileActivity) { user ->
                    val name = inputTextName.text.toString()
                    val username = inputTextUsername.text.toString()
                    val password = inputTextPassword.text.toString()
                    val token = user.token
                    updateResponse(token, username ,password, name)
                }
            }
        }
    }

    private fun updateResponse(token: String, username: String, password: String, name: String) {
        profileViewModel.updateUserData(token, username, password, name)
        profileViewModel.updateUserResponse.observe(this@UpdateProfileActivity) {
            if (!it.error) {
                showToast()
                finish()
            }
        }
    }

    private fun showToast() {
        profileViewModel.textToast.observe(this) {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}