package com.example.aksacarma.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aksacarma.data.remote.response.RegisterResponse
import com.example.aksacarma.repository.UserRepository
import com.example.aksacarma.ui.Event
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    val registerResponse: LiveData<RegisterResponse> = repository.registerResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    val textToast: LiveData<Event<String>> = repository.textToast
    fun registerUser(username: String, password: String, name: String, avatar: String) {
        viewModelScope.launch {
            repository.registerUser(username, password, name, avatar)
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            repository.logoutUser()
        }
    }
}