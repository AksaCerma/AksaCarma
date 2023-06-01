package com.example.aksacarma.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aksacarma.data.remote.response.LoginResponse
import com.example.aksacarma.model.UserModel
import com.example.aksacarma.repository.UserRepository
import com.example.aksacarma.ui.Event
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository): ViewModel() {
    val loginResponse: LiveData<LoginResponse> = repository.loginResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    val textToast: LiveData<Event<String>> = repository.textToast

    fun loginUser (username: String, password: String) {
        viewModelScope.launch {
            repository.loginUser(username, password)
        }
    }

    fun getLoginUser(user: UserModel) {
        viewModelScope.launch {
            repository.getLoginUser(user)
        }
    }

    fun getToken() {
        viewModelScope.launch {
            repository.getToken()
        }
    }
}

