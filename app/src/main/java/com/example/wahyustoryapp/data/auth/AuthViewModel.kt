package com.example.wahyustoryapp.data.auth

import androidx.lifecycle.*
import com.example.wahyustoryapp.data.retrofit.ApiConfig
import com.example.wahyustoryapp.data.retrofit.LoginForm
import kotlinx.coroutines.launch
import org.json.JSONObject

class AuthViewModel(val pref: AuthPreference) : ViewModel() {

    private val _isLoginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isLoginSuccess: LiveData<Boolean> get() = _isLoginSuccess

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> get() = _message

    fun isLogin(): LiveData<Boolean> {
        return pref.isLogin().asLiveData()
    }

    fun login() {
        viewModelScope.launch {
            val response = ApiConfig
                .getApiService()
                .getLoginData(LoginForm("debugk@gmail.com", "123456"))

            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    pref.writeToken(it.loginResult.token)
                    pref.login()
                    _isLoginSuccess.value = true
                    _message.value = it.message
                }
            } else {
                try {
                    val responseBody = response.errorBody()
                    responseBody?.let {
                        val obj = JSONObject(it.string())
                        _message.value = obj.getString("message")
                    }
                } catch (e: Exception) {
                    _message.value = "Terjadi Kesalahan Pada Server"
                }
                _isLoginSuccess.value = false
            }
        }
    }
}

class AuthViewModelFactory private constructor(private val prefs: AuthPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(prefs) as T
        }
        return super.create(modelClass)
    }

    companion object {
        private var INSTANCE: AuthViewModelFactory? = null
        fun getInstance(prefs: AuthPreference): AuthViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthViewModelFactory(prefs)
                INSTANCE = instance
                instance
            }
        }
    }
}