package com.example.aksacarma.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.aksacarma.data.remote.response.*
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

    private val _historyResponse = MutableLiveData<List<HistoryResponseItem>>()
    val historyResponse : LiveData<List<HistoryResponseItem>> = _historyResponse

    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse : LiveData<UserResponse> = _userResponse

    private val _userDataResponse = MutableLiveData<UserData>()
    val userDataResponse : LiveData<UserData> = _userDataResponse

    private val _updateUserResponse = MutableLiveData<UpdateUserResponse>()
    val updateUserResponse : LiveData<UpdateUserResponse> = _updateUserResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _textToast = MutableLiveData<Event<String>>()
    val textToast: LiveData<Event<String>> = _textToast

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
                    _textToast.value = Event("Username atau password salah!")
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
                    _textToast.value = Event("Gagal Mengirim Gambar")
                    Log.e(TAG,"onFailure: ${response.message()}, ${response.body()?.message.toString()}")
                }
            }
            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                _isLoading.value = false
                _textToast.value = Event("Bisa dicoba kembali")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getHistory(token: String) {
        _isLoading.value = true
        val client = apiService.getHistoryUser(token)
        client.enqueue(object : Callback<List<HistoryResponseItem>> {
            override fun onResponse(
                call: Call<List<HistoryResponseItem>>,
                response: Response<List<HistoryResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _historyResponse.value = response.body()
                } else {
                    _textToast.value = Event("Bisa dicoba kembali")
                    Log.e(TAG,"onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<HistoryResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _textToast.value = Event("Tidak Terhubung ke Internet")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserData(token: String) {
        _isLoading.value = true
        val client = apiService.getUserData(token)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userResponse.value = response.body()
                } else {
                    _textToast.value = Event("Bisa dicoba kembali")
                    Log.e(TAG,"onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _textToast.value = Event("Tidak Terhubung ke Internet")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun updateUser(token: String, avatar_image:MultipartBody.Part) {
        _isLoading.value = true
        val client = apiService.updateUser(token, avatar_image)
        client.enqueue(object : Callback<UpdateUserResponse> {
            override fun onResponse(call: Call<UpdateUserResponse>, response: Response<UpdateUserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _textToast.value = Event("Berhasil Update Profile")
                    _updateUserResponse.value = response.body()
                } else {
                    _textToast.value = Event("Gagal Update Profile")
                    Log.e(TAG,"onFailure: ${response.message()}, ${response.body()?.message.toString()}")
                }
            }
            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                _isLoading.value = false
                _textToast.value = Event("Bisa dicoba kembali")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun updateDataUser(token: String, username: String, password: String, name: String) {
        _isLoading.value = true
        val client = apiService.updateDataUser(token, username, password, name)
        client.enqueue(object : Callback<UpdateUserResponse> {
            override fun onResponse(call: Call<UpdateUserResponse>, response: Response<UpdateUserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _textToast.value = Event("Berhasil Update Profile")
                    _updateUserResponse.value = response.body()
                } else {
                    _textToast.value = Event("Gagal Update Profile")
                    Log.e(TAG,"onFailure: ${response.message()}, ${response.body()?.message.toString()}")
                }
            }
            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                _isLoading.value = false
                _textToast.value = Event("Bisa dicoba kembali")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

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