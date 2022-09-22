package com.example.wahyustoryapp.ui.main.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wahyustoryapp.data.repository.StoryRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : ViewModel() {
    private val repository = StoryRepository(application)
    val story = repository.storyDatabase
    val message = repository.message

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        refreshDatabase()
    }

    fun refreshDatabase(
        page: Int? = null,
        size: Int? = null,
        location: Boolean = false
    ) {
        _isLoading.value = false
        viewModelScope.launch {
            repository.refreshRepositoryData(
                page = page,
                size = size,
                withLocation = location
            )
            _isLoading.value = true
        }
    }

}

@Suppress("UNCHECKED_CAST")
class ApplicationFactory(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(application) as T
        }
        return super.create(modelClass)
    }
}