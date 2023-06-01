package com.example.aksacarma.data.remote.retrofit

import com.example.aksacarma.data.remote.response.LoginResponse
import com.example.aksacarma.data.remote.response.PredictionResponse
import com.example.aksacarma.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun registerUser(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("name") name: String,
        @Query("avatar_url") avatar: String
    ) : Call<RegisterResponse>

    @POST("login")
    fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("api-key") token: String,
        @Part file: MultipartBody.Part
    ): Call<PredictionResponse>
}