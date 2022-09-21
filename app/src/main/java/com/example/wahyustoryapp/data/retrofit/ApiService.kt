package com.example.wahyustoryapp.data.retrofit

import com.example.wahyustoryapp.data.retrofit.pojo.LoginResponse
import com.example.wahyustoryapp.data.retrofit.pojo.NormalResponse
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
