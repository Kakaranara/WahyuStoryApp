package com.example.wahyustoryapp.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val loginKey = booleanPreferencesKey("login")

    fun isLogin(): Flow<Boolean> {
        return dataStore.data.map {
            it[loginKey] ?: false
        }
    }

    suspend fun login() {
        dataStore.edit {
            it[loginKey] = true
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it[loginKey] = false
        }
    }

    companion object {
        var INSTANCE: AuthPreference? = null
        fun getInstance(dataStore: DataStore<Preferences>): AuthPreference {
            return INSTANCE ?: synchronized(this) {
                val inst = AuthPreference(dataStore)
                INSTANCE = inst
                inst
            }
        }
    }
}