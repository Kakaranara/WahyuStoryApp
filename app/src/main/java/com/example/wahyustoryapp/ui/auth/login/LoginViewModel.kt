package com.example.wahyustoryapp.data.auth

import androidx.lifecycle.*
import com.example.wahyustoryapp.data.network.ApiConfig
import com.example.wahyustoryapp.data.network.LoginForm
import com.example.wahyustoryapp.preferences.AuthPreference
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel(private val pref: AuthPreference) : ViewModel() {

    private val _isLoginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isLoginSuccess: LiveData<Boolean> = _isLoginSuccess

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(data: LoginForm) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getLoginData(data)
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        pref.writeToken(it.loginResult.token)
                        pref.login()
                        _isLoginSuccess.postValue(true)
                        _message.value = it.message
                    }
                } else {
                    val responseBody = response.errorBody()
                    responseBody?.let {
                        val obj = JSONObject(it.string())
                        _message.value = obj.getString("message")
                    }
                    _isLoginSuccess.postValue(false)
                }
            } catch (e: Exception) {
                _message.value = "Terjadi Kesalahan Pada Server"
                _isLoginSuccess.postValue(false)
            }

        }
    }
}

class AuthViewModelFactory private constructor(private val prefs: AuthPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(prefs) as T
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