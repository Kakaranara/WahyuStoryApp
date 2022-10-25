package com.example.wahyustoryapp.data.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.wahyustoryapp.data.network.ApiConfig
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.data.network.LoginForm
import com.example.wahyustoryapp.data.network.response.LoginResponse
import com.example.wahyustoryapp.helper.Async
import com.example.wahyustoryapp.preferences.AuthPreference
import org.json.JSONObject

class LoginRepository(
    private val apiService: ApiService,
    private val pref: AuthPreference
) {
    fun login(loginForm: LoginForm): LiveData<Async<LoginResponse>> = liveData {
        emit((Async.Loading))
        try {
            val response = ApiConfig.getApiService().getLoginData(loginForm)
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
}