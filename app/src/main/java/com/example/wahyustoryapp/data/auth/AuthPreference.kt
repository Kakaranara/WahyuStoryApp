package com.example.wahyustoryapp.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val loginKey = booleanPreferencesKey("login")
    private val tokenKey = stringPreferencesKey("token")

    fun isLogin(): Flow<Boolean> {
        return dataStore.data.map {
            it[loginKey] ?: false
        }
    }

    fun getToken(): Flow<String>{
        return dataStore.data.map {
            it[tokenKey] ?: "no token"
        }
    }


    suspend fun writeToken(token: String){
        dataStore.edit {
            it[tokenKey] = token
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
            it[tokenKey] = "No Token"
        }
    }

    companion object {
        @Volatile
        var INSTANCE: AuthPreference? = null

        @JvmStatic
        fun getInstance(dataStore: DataStore<Preferences>): AuthPreference {
            return INSTANCE ?: synchronized(this) {
                val inst = AuthPreference(dataStore)
                INSTANCE = inst
                inst
            }
        }
    }
}