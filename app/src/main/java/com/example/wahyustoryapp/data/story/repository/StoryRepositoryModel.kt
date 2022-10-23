package com.example.wahyustoryapp.data.story.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.data.network.response.NormalResponse
import com.google.android.gms.maps.model.LatLng
import retrofit2.Response
import java.io.File

interface StoryRepositoryModel {
    suspend fun refreshRepositoryData(
        page: Int? = null,
        size: Int? = null,
        withLocation: Boolean = false
    )

    suspend fun addStory(file: File, description: String, latLng: LatLng? = null) : Response<NormalResponse>

    fun getStoryData() : LiveData<PagingData<Story>>

    suspend fun clearDb()
}