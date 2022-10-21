package com.example.wahyustoryapp.ui.main.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wahyustoryapp.data.network.ApiConfig
import kotlinx.coroutines.launch

class MapsViewModel(private val token: String) : ViewModel() {
    fun requestApisLocation() {
        viewModelScope.launch {

        }
    }
}