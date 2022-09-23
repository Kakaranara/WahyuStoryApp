package com.example.wahyustoryapp.ui.main.addStory

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddStoryViewModel : ViewModel() {
    private val _photo: MutableLiveData<Bitmap> = MutableLiveData()
    val photo: LiveData<Bitmap> get() = _photo

    init {

    }

    fun insertPhoto(bitmap: Bitmap){
        _photo.value = bitmap
    }
}