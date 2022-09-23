package com.example.wahyustoryapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wahyustoryapp.authDataStore
import com.example.wahyustoryapp.data.auth.AuthPreference
import com.example.wahyustoryapp.data.database.StoryDao
import com.example.wahyustoryapp.data.database.StoryRoomDatabase
import com.example.wahyustoryapp.data.network.ApiConfig
import com.example.wahyustoryapp.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

class StoryRepository(application: Application) {
    private val dao: StoryDao
    private var token: String

    init {
        val db = StoryRoomDatabase.getInstance(application)
        dao = db.storyDao()
        val authDataStore = AuthPreference.getInstance(application.authDataStore)
        runBlocking {
            token = authDataStore.getToken().first()
        }
    }

    internal val storyDatabase = dao.getAllStories()

    private val _isError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isError: LiveData<Boolean> get() = _isError

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
            val networkData = ApiConfig.getApiService()
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
        val imgPart = MultipartBody.Part.createFormData("mPhoto", file.name)

        withContext(Dispatchers.Main) {
            val network = ApiConfig.getApiService().uploadImage(
                imgPart, requestDesc
            )
            if(network.isSuccessful){
                network.body()?.let {
                    _message.postValue(it.message)
                }
            }else{
                network.errorBody()?.let {
                    val obj = JSONObject(it.string())
                    _message.postValue(obj.getString("message"))
                }
            }
        }
    }
}