package com.example.wahyustoryapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.wahyustoryapp.preferences.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel(private val settingsPreferences: SettingPreferences) : ViewModel() {
    fun getTheme(): LiveData<Boolean> = settingsPreferences.getThemeSettings().asLiveData()
    fun writeTheme(darkMode : Boolean){
        viewModelScope.launch {
            settingsPreferences.saveThemeSetting(darkMode)
        }
    }
}