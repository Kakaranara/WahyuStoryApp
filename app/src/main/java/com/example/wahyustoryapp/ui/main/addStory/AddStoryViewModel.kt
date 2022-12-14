package com.example.wahyustoryapp.ui.main.addStory

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wahyustoryapp.data.network.response.NormalResponse
import com.example.wahyustoryapp.data.repository.StoryRepository
import com.example.wahyustoryapp.decodeToBitmap
import com.example.wahyustoryapp.helper.Async
import com.example.wahyustoryapp.utils.FileUtilityProcessing
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File

class AddStoryViewModel(
    private val repository: StoryRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    /**
     * * This class can't be unit tested
     * * Because the function need file and bitmap
     * * Which is need context
     * ? Might be refactored later -> with fragment result
     */

    private val _posting: MutableLiveData<Async<Response<NormalResponse>>> = MutableLiveData()
    val posting: LiveData<Async<Response<NormalResponse>>> get() = _posting

    private val _photo: MutableLiveData<Bitmap> = MutableLiveData()
    val photo: LiveData<Bitmap> get() = _photo

    private val _file: MutableLiveData<File> = MutableLiveData()
    val file: LiveData<File> get() = _file

    private val _isCompressing: MutableLiveData<Boolean> = MutableLiveData()
    val isCompressing: LiveData<Boolean> get() = _isCompressing


    fun uploadToServer(file: File, description: String, latLng: LatLng?) {
        viewModelScope.launch {
            _posting.postValue(Async.Loading)
            try {
                val response = repository.addStory(file, description, latLng)
                if (response.isSuccessful) {
                    _posting.postValue(Async.Success(response))
                    repository.refreshRepositoryData()
                } else {
                    _posting.postValue(Async.Error(response.message()))
                }
            } catch (e: Exception) {
                _posting.postValue(Async.Error("Please check your internet connection"))
            }

        }
    }

    fun processGalleryFile(file: File, util: FileUtilityProcessing = FileUtilityProcessing) {
        _photo.value = file.decodeToBitmap()
        viewModelScope.launch(dispatcher) {
            _isCompressing.postValue(true)
            val reduced = util.reduceFileImage(file)
            _file.postValue(reduced)
            _isCompressing.postValue(false)
        }
    }

    fun processCameraFileFromBitmap(
        bitmap: Bitmap,
        application: Application,
        util: FileUtilityProcessing = FileUtilityProcessing
    ) {
        _photo.value = bitmap
        viewModelScope.launch(dispatcher) {
            _isCompressing.postValue(true)
            val file = util.reduceFileImage(bitmap, application)
            _file.postValue(file)
            _isCompressing.postValue(false)
        }
    }
}