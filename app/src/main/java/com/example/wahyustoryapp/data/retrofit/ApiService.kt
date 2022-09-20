package com.example.wahyustoryapp.data.retrofit

import com.example.wahyustoryapp.data.pojo.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST

interface ApiService {

    @Multipart
    @POST
    fun postData()

    @POST("login")
    fun getLoginData(
        @Body form: LoginForm
    ) : Call<LoginResponse>

}

data class LoginForm(
    val email: String,
    val password: String
)