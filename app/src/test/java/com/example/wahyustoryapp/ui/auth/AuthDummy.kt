package com.example.wahyustoryapp.ui.auth

import com.example.wahyustoryapp.data.network.LoginForm
import com.example.wahyustoryapp.data.network.RegisterForm
import com.example.wahyustoryapp.data.network.response.LoginResponse
import com.example.wahyustoryapp.data.network.response.LoginResult
import com.example.wahyustoryapp.data.network.response.NormalResponse

object AuthDummy {
    fun provideLoginForm(): LoginForm = LoginForm("a@gmail.com", "123456")


    fun provideRegisterResponse() : NormalResponse = NormalResponse(false, "ok")

    fun provideRegisterForm(): RegisterForm =
        RegisterForm("debugk", "wahyuKoco@gmail.com", "123456")

    fun provideLoginResponse(): LoginResponse =
        LoginResponse(LoginResult("", "IDK412", "ABCD"), false, "ok")
}