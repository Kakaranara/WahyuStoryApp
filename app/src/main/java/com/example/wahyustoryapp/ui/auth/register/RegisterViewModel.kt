package com.example.wahyustoryapp.ui.auth.register

import androidx.lifecycle.ViewModel
import com.example.wahyustoryapp.data.network.RegisterForm
import com.example.wahyustoryapp.data.repository.AuthRepository

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun registerAccount(form: RegisterForm) = authRepository.register(form)
}