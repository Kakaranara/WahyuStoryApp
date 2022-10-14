package com.example.wahyustoryapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wahyustoryapp.data.database.StoryDao
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

class StoryRepository(
    private val dao: StoryDao,
    private var service: ApiService,
    private var token: String
) {

    internal val storyData = dao.getAllStories()

    private val _isError: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> get() = _message

    private val _isFetching: MutableLiveData<Boolean> = MutableLiveData()
    val isFetching: LiveData<Boolean> get() = _isFetching

    suspend fun refreshRepositoryData(
        page: Int? = null,
        size: Int? = null,
        withLocation: Boolean = false
    ) {
        val location = when (withLocation) {
            false -> 0
            true -> 1
        }
        withContext(Dispatchers.IO) {
            _isFetching.postValue(true)
            val networkData = service
                .getAllStory(
                    "Bearer $token",
                    page = page,
                    size = size,
                    location = location
                )
            _isFetching.postValue(true)
            if (networkData.isSuccessful) {
                dao.deleteAll()
                _isError.postValue(false)
                networkData.body()?.let { response ->
                    _message.postValue(response.message)
                    val result = response.listStory.toEntity()
                    dao.insertAll(result)
                }
            } else {
                try {
                    networkData.errorBody()?.let {
                        val obj = JSONObject(it.string())
                        _message.postValue(obj.getString("message"))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _message.postValue("Terjadi suatu kesalahan pada server")
                }
                _isError.value = true
            }

        }
    }

    suspend fun addStory(file: File, description: String) {

        val requestDesc = description.toRequestBody("text/plain".toMediaType())
        val requestImage = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imgPart = MultipartBody.Part.createFormData("photo", file.name, requestImage)

        withContext(Dispatchers.Main) {
            try {
                _isFetching.postValue(true)
                val network = service.uploadImage(
                    "Bearer $token",
                    imgPart, requestDesc
                )
                if (network.isSuccessful) {
                    refreshRepositoryData()
                    network.body()?.let {
                        _isSuccess.postValue(true)
                        _message.postValue(it.message)
                    }
                } else {
                    network.errorBody()?.let {
                        val obj = JSONObject(it.string())
                        _isSuccess.postValue(false)
                        _message.postValue(obj.getString("message"))
                    }
                }
                _isFetching.postValue(false)
            } catch (e: Exception) {
                e.printStackTrace()
                _isSuccess.postValue(false)
                _message.postValue("Harap cek koneksi anda")
                _isFetching.postValue(false)
                Log.e("REPO", "uploadToServer: $e")
            }
        }
    }

    suspend fun clearDb() {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }

}