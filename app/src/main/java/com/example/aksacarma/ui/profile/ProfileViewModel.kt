package com.example.aksacarma.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aksacarma.data.remote.response.*
import com.example.aksacarma.model.UserModel
import com.example.aksacarma.repository.UserRepository
import com.example.aksacarma.ui.Event
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    val updateUserResponse : LiveData<UpdateUserResponse> = repository.updateUserResponse
    val userResponse: LiveData<UserResponse> = repository.userResponse
    val textToast: LiveData<Event<String>> = repository.textToast

    fun getUserData(token: String) {
        viewModelScope.launch {
            repository.getUserData(token)
        }
    }
    fun updateUser(token: String, avatar_image: MultipartBody.Part) {
        viewModelScope.launch {
            repository.updateUser(token, avatar_image)
        }
    }

    fun getUser(): LiveData<UserModel> {
        return repository.getUser()
    }
    fun logoutUser() {
        viewModelScope.launch {
            repository.logoutUser()
        }
    }
}