package com.example.wahyustoryapp.ui.main.addStory

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wahyustoryapp.data.repository.StoryRepository
import com.example.wahyustoryapp.reduceFileImage
import kotlinx.coroutines.launch
import java.io.File

class AddStoryViewModel(application: Application) : ViewModel() {
    private val repository = StoryRepository(application)
    val isUploading = repository.isFetching
    val message = repository.message

    private val _photo: MutableLiveData<Bitmap> = MutableLiveData()
    val photo: LiveData<Bitmap> get() = _photo

    private val _file: MutableLiveData<File> = MutableLiveData()
    val file: LiveData<File> get() = _file


    fun uploadToServer(file: File, description: String) {
        viewModelScope.launch {
            repository.addStory(file, description)
        }
    }

    fun insertFile(file: File) {
        val reduced = reduceFileImage(file)
        _file.value = reduced
    }

    fun insertPhoto(bitmap: Bitmap) {
        _photo.value = bitmap
    }
}