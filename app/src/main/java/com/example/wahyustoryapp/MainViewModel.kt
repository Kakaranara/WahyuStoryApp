package com.example.wahyustoryapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.wahyustoryapp.preferences.SettingPreferences
import com.example.wahyustoryapp.ui.SettingViewModel

class MainViewModel(private val settingPreferences: SettingPreferences) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return settingPreferences.getThemeSettings().asLiveData()
    }

}

@Suppress("UNCHECKED_CAST")
class SettingsFactory private constructor(private val settingPreferences: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(settingPreferences) as T
        }
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(settingPreferences) as T
        }
        return super.create(modelClass)
    }

    companion object {
        private var INSTANCE: SettingsFactory? = null
        fun getInstance(prefs: SettingPreferences): SettingsFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsFactory(prefs)
                INSTANCE = instance
                instance
            }
        }
    }
}