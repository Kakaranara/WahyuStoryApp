package com.example.wahyustoryapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wahyustoryapp.preferences.SettingPreferences
import com.example.wahyustoryapp.ui.main.addStory.AddStoryViewModel
import com.example.wahyustoryapp.ui.main.home.HomeViewModel
import com.example.wahyustoryapp.ui.settings.SettingViewModel

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

@Suppress("UNCHECKED_CAST")
class ApplicationFactory(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(application) as T
        }
        if(modelClass.isAssignableFrom(AddStoryViewModel::class.java)){
            return AddStoryViewModel(application) as T
        }
        return super.create(modelClass)
    }
}