package com.example.aksacarma.data.remote.retrofit

import com.example.aksacarma.data.remote.response.LoginResponse
import com.example.aksacarma.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

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
}