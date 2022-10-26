package com.example.wahyustoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.data.network.LoginForm
import com.example.wahyustoryapp.data.network.RegisterForm
import com.example.wahyustoryapp.data.network.response.LoginResponse
import com.example.wahyustoryapp.data.network.response.NormalResponse
import com.example.wahyustoryapp.data.repository.model.AuthRepositoryModel
import com.example.wahyustoryapp.helper.Async
import com.example.wahyustoryapp.preferences.AuthPreference
import org.json.JSONObject

class AuthRepository(
    private val apiService: ApiService,
    private val pref: AuthPreference
) : AuthRepositoryModel {
    override fun login(loginForm: LoginForm): LiveData<Async<LoginResponse>> = liveData {
        emit((Async.Loading))
        try {
            val response = apiService.getLoginData(loginForm)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    pref.writeToken(it.loginResult.token)
                    pref.login()
                    emit(Async.Success(it))
                }
            } else {
                response.errorBody()?.let {
                    val error = JSONObject(it.string())
                    emit(Async.Error(error.getString("message")))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Async.Error("Terjadi suatu masalah"))
        }
    }

    override fun register(registerForm: RegisterForm): LiveData<Async<NormalResponse>> = liveData {
        emit((Async.Loading))
        try {
            val response = apiService.registerUser(registerForm)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Async.Success(it))
                }
            } else {
                response.errorBody()?.let {
                    val error = JSONObject(it.string())
                    emit(Async.Error(error.getString("message")))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Async.Error("Terjadi suatu masalah"))
        }
    }
}