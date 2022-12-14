package com.example.wahyustoryapp.ui.main.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.wahyustoryapp.data.network.response.ListStoryItem
import com.example.wahyustoryapp.data.repository.MapsRepository
import com.example.wahyustoryapp.helper.Async

class MapsViewModel(private val repository: MapsRepository) : ViewModel() {

    fun data(): LiveData<Async<List<ListStoryItem>>> = repository.requestApisLocation()

}