package com.example.wahyustoryapp.ui.auth.login

import androidx.lifecycle.ViewModel
import com.example.wahyustoryapp.data.repository.LoginRepository
import com.example.wahyustoryapp.data.network.LoginForm

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    fun loginEvent(loginForm: LoginForm) = repository.login(loginForm)

}

