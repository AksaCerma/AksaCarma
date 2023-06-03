package com.example.aksacarma.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aksacarma.model.UserModel
import com.example.aksacarma.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return repository.getUser()
    }
}