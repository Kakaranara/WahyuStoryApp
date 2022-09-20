package com.example.wahyustoryapp.data.auth

import androidx.lifecycle.*
import com.example.wahyustoryapp.data.pojo.LoginResponse
import retrofit2.Callback
import com.example.wahyustoryapp.data.retrofit.ApiConfig
import com.example.wahyustoryapp.data.retrofit.LoginForm
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class AuthViewModel(val pref: AuthPreference) : ViewModel() {

    val _isLoginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isLoginSuccess: LiveData<Boolean> get() = _isLoginSuccess

    fun isLogin(): LiveData<Boolean> {
        return pref.isLogin().asLiveData()
    }

    fun login(){
        val client = ApiConfig.getApiService().getLoginData(LoginForm("debugk@gmail.com","123456"))

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        viewModelScope.launch {
                            pref.writeToken(it.loginResult.token)
                            pref.login()
                            _isLoginSuccess.value = true
                        }
                    }
                } else {
                    _isLoginSuccess.value = false
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                throw Exception("Failed fetch!")
            }
        })
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
        fun getInstance(prefs: AuthPreference) : AuthViewModelFactory{
            return INSTANCE ?: synchronized(this){
                val instance = AuthViewModelFactory(prefs)
                INSTANCE = instance
                instance
            }
        }
    }
}