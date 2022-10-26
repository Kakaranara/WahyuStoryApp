package com.example.wahyustoryapp.data.repository.model

import androidx.lifecycle.LiveData
import com.example.wahyustoryapp.data.network.LoginForm
import com.example.wahyustoryapp.data.network.RegisterForm
import com.example.wahyustoryapp.data.network.response.LoginResponse
import com.example.wahyustoryapp.data.network.response.NormalResponse
import com.example.wahyustoryapp.helper.Async

interface AuthRepositoryModel {
    fun login(loginForm: LoginForm): LiveData<Async<LoginResponse>>
    fun register(registerForm: RegisterForm): LiveData<Async<NormalResponse>>
}
