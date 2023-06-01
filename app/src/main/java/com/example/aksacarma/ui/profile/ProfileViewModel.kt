package com.example.aksacarma.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aksacarma.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    fun logoutUser() {
        viewModelScope.launch {
            repository.logoutUser()
        }
    }
}