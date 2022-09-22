package com.example.wahyustoryapp.ui.main.home

import android.accounts.NetworkErrorException
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

    private val _isNetworkError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNetworkError: LiveData<Boolean> get() = _isNetworkError

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
            try {
                repository.refreshRepositoryData(
                    page = page,
                    size = size,
                    withLocation = location
                )
                _isNetworkError.value = false
                _isLoading.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _isNetworkError.value = true
                _isLoading.value = false
            }

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