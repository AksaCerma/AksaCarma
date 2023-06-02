package com.example.aksacarma.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aksacarma.data.remote.response.LoginResponse
import com.example.aksacarma.data.remote.response.PredictionResponse
import com.example.aksacarma.model.UserModel
import com.example.aksacarma.repository.UserRepository
import com.example.aksacarma.ui.Event
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class CameraViewModel(private val repository: UserRepository): ViewModel() {
    val predictionResponse: LiveData<PredictionResponse> = repository.predictionResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    val textToast: LiveData<Event<String>> = repository.textToast

    fun uploadImage(token: String, image: MultipartBody.Part) {
        viewModelScope.launch {
            repository.uploadImage(token, image)
        }
    }

    fun getUser(): LiveData<UserModel> {
        return repository.getUser()
    }

}