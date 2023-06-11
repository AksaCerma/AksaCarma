package com.example.aksacarma.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aksacarma.data.remote.response.HistoryResponseItem
import com.example.aksacarma.model.UserModel
import com.example.aksacarma.repository.UserRepository
import com.example.aksacarma.ui.Event
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: UserRepository) : ViewModel()  {
    val historyResponse: LiveData<List<HistoryResponseItem>> = repository.historyResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    val textToast: LiveData<Event<String>> = repository.textToast

    fun getHistory(token: String) {
        viewModelScope.launch {
            repository.getHistory(token)
        }
    }

    fun getUser(): LiveData<UserModel> {
        return repository.getUser()
    }
}