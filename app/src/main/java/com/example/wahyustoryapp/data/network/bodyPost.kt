package com.example.wahyustoryapp.data.network


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