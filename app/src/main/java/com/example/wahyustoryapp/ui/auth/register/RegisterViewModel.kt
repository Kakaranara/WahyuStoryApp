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
import org.json.JSONObject
import retrofit2.Response

class RegisterViewModel : ViewModel() {

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
                    response.errorBody()?.let {
                        val error = JSONObject(it.string())
                        _registerEvent.postValue(Async.Error(error.getString("message")))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _registerEvent.postValue(Async.Error("Terjadi suatu masalah"))
            }
        }
    }
}