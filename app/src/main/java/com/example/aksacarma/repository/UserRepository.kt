package com.example.aksacarma.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.aksacarma.data.remote.response.LoginResponse
import com.example.aksacarma.data.remote.response.PredictionResponse
import com.example.aksacarma.data.remote.response.PredictionResult
import com.example.aksacarma.data.remote.response.RegisterResponse
import com.example.aksacarma.data.remote.retrofit.ApiService
import com.example.aksacarma.model.UserModel
import com.example.aksacarma.model.UserPreferences
import com.example.aksacarma.ui.Event
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository constructor(
    private val apiService: ApiService,
    private val preferences: UserPreferences
) {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _predictionResponse = MutableLiveData<PredictionResponse>()
    val predictionResponse: LiveData<PredictionResponse> = _predictionResponse

    private val _predictionResult = MutableLiveData<PredictionResult>()
    val predictionResult: LiveData<PredictionResult> = _predictionResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _textToast = MutableLiveData<Event<String>>()
    val textToast: LiveData<Event<String>> = _textToast

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun registerUser(username: String, password: String, name: String, avatar: String) {
        _isLoading.value = true
        val client = apiService.registerUser(username, password, name, avatar)
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

    fun loginUser(username: String, password: String) {
        _isLoading.value = true
        val client = apiService.loginUser(username, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _textToast.value = Event("Login Berhasil")
                    _loginResponse.value = response.body()
                } else {
                    _textToast.value = Event("Gagal Login")
                    Log.e(TAG,"onFailure: ${response.message()}, ${response.body()?.message.toString()}")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _textToast.value = Event("Tidak Terhubung ke Internet")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun uploadImage(token: String, image:MultipartBody.Part) {
        _isLoading.value = true
        val client = apiService.uploadImage(token, image)
        client.enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _textToast.value = Event("Berhasil Mengirim Gambar")
                    _predictionResponse.value = response.body()
                } else {
//                    _textToast.value = Event("Gagal Login")
                    _errorMessage.value = response.message()
                    Log.e(TAG,"onFailure: ${response.message()}, ${response.body()?.message.toString()}")
                }
            }
            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
//                _textToast.value = Event("Tidak Terhubung ke Internet")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

//    suspend fun getResult(token: String, prediction: String) {
//        _isLoading.value = true
//        val client = apiService.getResult(token, prediction)
//        client.enqueue(object : Callback<PredictionResult> {
//            override fun onResponse(call: Call<PredictionResult>, response: Response<PredictionResult>) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _textToast.value = Event("Login Berhasil")
//                    _predictionResult.value = response.body()
//                } else {
//                    _textToast.value = Event("Gagal Login")
//                    Log.e(TAG,"onFailure: ${response.message()}, ${response.body()?.message.toString()}")
//                }
//            }
//            override fun onFailure(call: Call<PredictionResult>, t: Throwable) {
//                _isLoading.value = false
//                _textToast.value = Event("Tidak Terhubung ke Internet")
//                Log.e(TAG, "onFailure: ${t.message.toString()}")
//            }
//        })
//    }

    fun getUser(): LiveData<UserModel> {
        return preferences.getUser().asLiveData()
    }

    suspend fun getLoginUser(user: UserModel) {
        preferences.getLoginUser(user)
    }

    suspend fun getToken() {
        preferences.getToken()
    }

    suspend fun logoutUser() {
        preferences.logoutUser()
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