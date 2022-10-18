package com.example.wahyustoryapp.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wahyustoryapp.data.repository.StoryRepository
import com.example.wahyustoryapp.helper.Async
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: StoryRepository) : ViewModel() {
    val story = repository.getStoryData()

    private val _refreshDb: MutableLiveData<Async<Boolean>> = MutableLiveData()
    val refreshDb: LiveData<Async<Boolean>> = _refreshDb

    init {
        refreshDatabase(size = 40)
    }

    fun clearDatabase() {
        viewModelScope.launch {
            repository.clearDb()
        }
    }

    fun refreshDatabase(
        page: Int? = null,
        size: Int? = null,
        location: Boolean = false
    ) {
        _refreshDb.postValue(Async.Loading)
        viewModelScope.launch {
            try {
                repository.refreshRepositoryData(
                    page = page,
                    size = size,
                    withLocation = location
                )
                _refreshDb.postValue(Async.Success(true))
            } catch (e: Exception) {
                e.printStackTrace()
                _refreshDb.postValue(Async.Error("Couldn't Refresh Data"))
            }

        }
    }

}

