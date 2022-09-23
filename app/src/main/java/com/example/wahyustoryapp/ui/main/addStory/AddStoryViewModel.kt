package com.example.wahyustoryapp.ui.main.addStory

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wahyustoryapp.data.network.ApiConfig
import com.example.wahyustoryapp.data.repository.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

class AddStoryViewModel(application: Application) : ViewModel() {
    private val repository = StoryRepository(application)

    private val _photo: MutableLiveData<Bitmap> = MutableLiveData()
    val photo: LiveData<Bitmap> get() = _photo

    private val _file: MutableLiveData<File> = MutableLiveData()
    val file: LiveData<File> get() = _file

//    private val _message: MutableLiveData<Message> = MutableLiveData()
//    val message: LiveData<Message> get() = _message


    val message = repository.message

    fun uploadToServer(file: File, description: String) {
        viewModelScope.launch {
//            repository.addStory(image, description)

            val requestDesc = description.toRequestBody("text/plain".toMediaType())
            val requestImage = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val imgPart = MultipartBody.Part.createFormData("mPhoto", file.name, requestImage)

            withContext(Dispatchers.Main) {
                try{
                    val network = ApiConfig.getApiService().uploadImage(
                        imgPart, requestDesc
                    )
                    if (network.isSuccessful) {
                        network.body()?.let {
//                        _message.postValue(it.message)
                        }
                    } else {
                        network.errorBody()?.let {
                            val obj = JSONObject(it.string())
//                        _message.postValue(obj.getString("message"))
                        }
                    }
                }
                catch (e: Exception){
                    e.printStackTrace()
                    Log.e("ERRSS", "uploadToServer: $e", )
                }
            }
        }
    }

    fun insertFile(file: File) {
        _file.value = file
    }

    fun insertPhoto(bitmap: Bitmap) {
        _photo.value = bitmap
    }
}