package com.example.wahyustoryapp.data.repository.model

import androidx.lifecycle.LiveData
import com.example.wahyustoryapp.data.network.response.ListStoryItem
import com.example.wahyustoryapp.helper.Async

interface MapsRepositoryModel {
    fun requestApisLocation(): LiveData<Async<List<ListStoryItem>>>
}