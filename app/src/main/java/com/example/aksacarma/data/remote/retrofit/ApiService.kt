package com.example.aksacarma.data.remote.retrofit

import com.example.aksacarma.data.remote.response.LoginResponse
import com.example.aksacarma.data.remote.response.PredictionResponse
import com.example.aksacarma.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("avatar_url") avatar: String
    ) : Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("get_disease_name")
    fun uploadImage(
        @Header("api-key") token: String,
        @Part file: MultipartBody.Part
    ): Call<PredictionResponse>
}