package com.example.aksacarma.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aksacarma.data.remote.response.UserResponse
import com.example.aksacarma.model.UserModel
import com.example.aksacarma.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    val userResponse: LiveData<UserResponse> = repository.userResponse

    fun getUserData(token: String) {
        viewModelScope.launch {
            repository.getUserData(token)
        }
    }

    fun getUser(): LiveData<UserModel> {
        return repository.getUser()
    }
}