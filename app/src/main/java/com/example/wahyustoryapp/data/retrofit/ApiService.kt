package com.example.wahyustoryapp.data.retrofit

import com.example.wahyustoryapp.data.pojo.LoginResponse
import com.example.wahyustoryapp.data.pojo.NormalResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
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

//below is for POST related argument's

//for login
data class LoginForm(
    val email: String,
    val password: String
)

//for register
data class RegisterForm(
    val name: String,
    val email: String,
    val password: String
)