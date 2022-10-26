package com.example.wahyustoryapp.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.wahyustoryapp.data.repository.StoryRepository
import com.example.wahyustoryapp.helper.Async
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: StoryRepository) : ViewModel() {
    val story = repository.getStoryData().cachedIn(viewModelScope)

    private val _refreshDb: MutableLiveData<Async<Boolean>> = MutableLiveData()
    val refreshDb: LiveData<Async<Boolean>> = _refreshDb

    fun clearDatabase() {
        viewModelScope.launch {
            repository.clearDb()
        }
    }

}

