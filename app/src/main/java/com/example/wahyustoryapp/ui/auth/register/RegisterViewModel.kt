package com.example.wahyustoryapp.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wahyustoryapp.data.network.ApiConfig
import com.example.wahyustoryapp.data.network.RegisterForm
import com.example.wahyustoryapp.data.network.response.NormalResponse
import com.example.wahyustoryapp.helper.Async
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel : ViewModel() {


//    private var _isLoading: MutableLiveData<Boolean> = MutableLiveData()
//    val isLoading: LiveData<Boolean> get() = _isLoading
//
//    private var _message: MutableLiveData<String> = MutableLiveData()
//    val message: LiveData<String> get() = _message
//
//    private var _isRegisterSuccess: MutableLiveData<Boolean> = MutableLiveData()
//    val isRegisterSucces: LiveData<Boolean> get() = _isRegisterSuccess

    private val _registerEvent: MutableLiveData<Async<Response<NormalResponse>>> = MutableLiveData()
    val registerEvent: LiveData<Async<Response<NormalResponse>>> get() = _registerEvent

    fun registerAccount(form: RegisterForm) {
        viewModelScope.launch {
            _registerEvent.postValue(Async.Loading)
            try {
                val response = ApiConfig.getApiService().registerUser(form)
                if (response.isSuccessful) {
                    _registerEvent.postValue(Async.Success(response))
                } else {
                    _registerEvent.postValue(Async.Error(response.message()))
                }
            } catch (e: Exception) {
                _registerEvent.postValue(Async.Error("Terjadi suatu masalah"))
            }
//            _isLoading.value = true
//            val response = ApiConfig
//                .getApiService()
//                .registerUser(form)
//
//            if (response.isSuccessful) {
//                _isLoading.value = false
//                response.body()?.let {
//                    _message.value = it.message
//                    _isRegisterSuccess.value = true
//                }
//                _registerEvent.postValue(Async.Success(response))
//            } else {
//                _isLoading.value = false
//                _isRegisterSuccess.value = false
//                response.errorBody()?.let {
//                    try {
//                        val obj = JSONObject(it.string())
//                        _message.value = obj.getString("message")
//
//                    } catch (e: Exception) {
//                        _message.value = "Terjadi kesalahan pada server"
//                    }
//
//                }
//            }
        }
    }
}