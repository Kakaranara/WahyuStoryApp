package com.example.wahyustoryapp.data.network

import com.example.wahyustoryapp.data.network.response.LoginResponse
import com.example.wahyustoryapp.data.network.response.NormalResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST

interface ApiService {

    @Multipart
    @POST
    fun postData()

    @POST("login")
    suspend fun getLoginData(
        @Body form: LoginForm
    ): Response<LoginResponse>

    @POST("register")
    suspend fun registerUser(
        @Body form: RegisterForm
    ): Response<NormalResponse>

}
