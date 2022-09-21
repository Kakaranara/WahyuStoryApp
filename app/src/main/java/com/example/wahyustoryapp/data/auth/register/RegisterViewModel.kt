package com.example.wahyustoryapp.data.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wahyustoryapp.data.retrofit.ApiConfig
import com.example.wahyustoryapp.data.retrofit.RegisterForm
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.json.JSONObject

class RegisterViewModel : ViewModel() {

    private var _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> get() = _message

    private var _isRegisterSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isRegisterSucces: LiveData<Boolean> get() = _isRegisterSuccess

    fun registerAccount(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val response = ApiConfig
                .getApiService()
                .registerUser(RegisterForm(name, email, password))

            if (response.isSuccessful) {
                _isLoading.value = false
                response.body()?.let {
                    _message.value = it.message
                    _isRegisterSuccess.value = true
                }
            } else {
                _isLoading.value = false
                _isRegisterSuccess.value = false
                response.errorBody()?.let {
                    try {
                        val obj = JSONObject(it.string())
                        _message.value = obj.getString("message")
                    } catch (e: Exception) {
                        _message.value = "Terjadi kesalahan pada server"
                    }

                }
            }
        }
    }
}