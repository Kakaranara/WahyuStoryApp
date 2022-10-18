@file:Suppress("UNCHECKED_CAST")

package com.example.wahyustoryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wahyustoryapp.data.repository.StoryRepository
import com.example.wahyustoryapp.preferences.AuthPreference
import com.example.wahyustoryapp.preferences.SettingPreferences
import com.example.wahyustoryapp.ui.auth.login.LoginViewModel
import com.example.wahyustoryapp.ui.main.addStory.AddStoryViewModel
import com.example.wahyustoryapp.ui.main.home.HomeViewModel
import com.example.wahyustoryapp.ui.settings.SettingViewModel

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

class ViewModelFactory(private val repository: StoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        return super.create(modelClass)
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