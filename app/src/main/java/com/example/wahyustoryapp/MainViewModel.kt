package com.example.wahyustoryapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.wahyustoryapp.preferences.SettingPreferences
import kotlinx.coroutines.flow.first

class MainViewModel(private val settingPreferences: SettingPreferences) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return settingPreferences.getThemeSettings().asLiveData()
    }

    suspend fun getSingleThemeSettings() : Boolean{
        return settingPreferences.getThemeSettings().first()
    }

}

