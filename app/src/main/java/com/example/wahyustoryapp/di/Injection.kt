package com.example.wahyustoryapp.di

import android.content.Context
import com.example.wahyustoryapp.authDataStore
import com.example.wahyustoryapp.data.database.StoryRoomDatabase
import com.example.wahyustoryapp.data.network.ApiConfig
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.data.repository.StoryRepository
import com.example.wahyustoryapp.preferences.AuthPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository {
        val db = StoryRoomDatabase.getInstance(context.applicationContext)
        val dao = db.storyDao()
        val storyApi: ApiService = ApiConfig.getApiService()
        val token = runBlocking {
            AuthPreference.getInstance(context.authDataStore).getToken().first()
        }
        return StoryRepository(dao, storyApi, token)
    }
}