package com.example.wahyustoryapp.data.repository

import androidx.lifecycle.LiveData
import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.helper.*
import java.io.File

interface StoryRepositoryModel {
    suspend fun refreshRepositoryData(
        page: Int? = null,
        size: Int? = null,
        withLocation: Boolean = false
    ): LiveData<Async<Event<String>>>

    suspend fun addStory(file: File, description: String) : LiveData<Async<Event<String>>>

    fun getStoryData() : LiveData<List<Story>>
}