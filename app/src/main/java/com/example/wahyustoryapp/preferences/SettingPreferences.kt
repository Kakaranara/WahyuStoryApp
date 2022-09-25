package com.example.wahyustoryapp.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val themeKey = booleanPreferencesKey("theme")

    fun getThemeSettings(): Flow<Boolean> {
        return dataStore.data.map {
            it[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(bool: Boolean) {
        dataStore.edit {
            it[themeKey] = bool
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        @JvmStatic
        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}