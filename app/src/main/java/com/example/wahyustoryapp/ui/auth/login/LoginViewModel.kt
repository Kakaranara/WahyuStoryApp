package com.example.wahyustoryapp.ui.auth.login

import androidx.lifecycle.*
import com.example.wahyustoryapp.data.network.ApiConfig
import com.example.wahyustoryapp.data.network.LoginForm
import com.example.wahyustoryapp.data.network.response.LoginResponse
import com.example.wahyustoryapp.helper.Async
import com.example.wahyustoryapp.preferences.AuthPreference
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel(private val pref: AuthPreference) : ViewModel() {

    private val _loginEvent = MutableLiveData<Async<LoginResponse>>()
    val loginEvent: LiveData<Async<LoginResponse>> get() = _loginEvent

    fun login(data: LoginForm) {
        _loginEvent.postValue(Async.Loading)
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getLoginData(data)
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        pref.writeToken(it.loginResult.token)
                        pref.login()
                        _loginEvent.postValue(Async.Success(it))
                    }
                } else {
                    response.errorBody()?.let {
                        val error = JSONObject(it.string())
                        _loginEvent.postValue(Async.Error(error.getString("message")))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _loginEvent.postValue(Async.Error("Terjadi suatu masalah"))
            }
        }
    }
}

