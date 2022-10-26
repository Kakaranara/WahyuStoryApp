package com.example.wahyustoryapp.di

import android.content.Context
import com.example.wahyustoryapp.authDataStore
import com.example.wahyustoryapp.data.repository.AuthRepository
import com.example.wahyustoryapp.data.database.StoryRoomDatabase
import com.example.wahyustoryapp.data.network.ApiConfig
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.data.repository.MapsRepository
import com.example.wahyustoryapp.data.repository.StoryRepository
import com.example.wahyustoryapp.preferences.AuthPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository {
        val db = StoryRoomDatabase.getInstance(context.applicationContext)
        val storyApi: ApiService = ApiConfig.getApiService()
        val token = runBlocking {
            AuthPreference.getInstance(context.authDataStore).getToken().first()
        }
        return StoryRepository(db, storyApi, token)
    }

    fun provideMapsRepository(context: Context): MapsRepository {
        val token = runBlocking {
            AuthPreference.getInstance(context.authDataStore).getToken().first()
        }
        return MapsRepository(token, ApiConfig.getApiService())
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val preference = AuthPreference.getInstance(context.authDataStore)

        return AuthRepository(ApiConfig.getApiService(), preference)
    }
}