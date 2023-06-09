package com.example.aksacarma.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aksacarma.model.UserModel
import com.example.aksacarma.repository.UserRepository
import com.example.aksacarma.ui.Event
import com.example.aksacarma.ui.ViewModelFactory
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val repository: UserRepository): ViewModel() {
    val textToast: LiveData<Event<String>> = repository.textToast

    fun getUser(): LiveData<UserModel> {
        return repository.getUser()
    }
}