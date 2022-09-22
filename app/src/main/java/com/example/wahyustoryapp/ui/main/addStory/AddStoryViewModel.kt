package com.example.wahyustoryapp.ui.main.addStory

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AddStoryViewModel {
    private val _photo: MutableLiveData<Bitmap> = MutableLiveData()
    val photo: LiveData<Bitmap> get() = _photo

    init {

    }

    fun getPhoto(){

    }
}