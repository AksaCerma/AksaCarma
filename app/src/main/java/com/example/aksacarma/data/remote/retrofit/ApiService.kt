package com.example.aksacarma.data.remote.retrofit

import com.example.aksacarma.data.remote.response.*
import okhttp3.MultipartBody
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
    @POST("get-disease-name")
    fun uploadImage(
        @Header("token") token: String,
        @Part image: MultipartBody.Part
    ): Call<PredictionResponse>

    @GET("get-user-history")
    fun getHistoryUser(
        @Header("token") token: String,
    ): Call<List<HistoryResponseItem>>

    @GET("get-user-data")
    fun getUserData(
        @Header("token") token: String,
    ): Call<UserResponse>

    @Multipart
    @POST("update-user")
    fun updateUser(
        @Header("token") token: String,
        @Part avatar_image: MultipartBody.Part
    ): Call<UpdateUserResponse>
}