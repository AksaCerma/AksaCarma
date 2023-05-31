package com.example.aksacarma.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aksacarma.data.remote.response.RegisterResponse
import com.example.aksacarma.model.UserPreferences
import com.example.aksacarma.ui.Event
import com.setyo.storyapp.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository constructor(
    private val apiService: ApiService,
    private val preferences: UserPreferences
) {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _textToast = MutableLiveData<Event<String>>()
    val textToast: LiveData<Event<String>> = _textToast

    fun getDataRegister(name: String, email: String, password: String) {
        _isLoading.value = true
        val client = apiService.registerUser(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _textToast.value = Event("Selamat Anda Berhasil Registrasi")
                    _registerResponse.value = response.body()
                } else {
                    _textToast.value = Event("Akun Sudah Pernah Dibuat")
                    Log.e(TAG,"onFailure: ${response.message()}, ${response.body()?.message.toString()}")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _textToast.value = Event("Tidak Terhubung ke Internet")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "StoryRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(preferences: UserPreferences, apiService: ApiService): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, preferences)
            }.also { instance = it }
    }
}