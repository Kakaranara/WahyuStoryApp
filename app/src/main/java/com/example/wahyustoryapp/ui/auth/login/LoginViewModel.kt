package com.example.wahyustoryapp.ui.auth.login

import androidx.lifecycle.ViewModel
import com.example.wahyustoryapp.data.repository.AuthRepository
import com.example.wahyustoryapp.data.network.LoginForm

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    fun loginEvent(loginForm: LoginForm) = repository.login(loginForm)

}

