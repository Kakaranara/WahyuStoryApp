package com.example.wahyustoryapp.data.story.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.data.network.response.ListStoryItem
import com.example.wahyustoryapp.helper.Async
import org.json.JSONObject

class MapsRepository(
    private val token: String,
    private val apiService: ApiService
) {
    fun requestApisLocation(): LiveData<Async<List<ListStoryItem>>> = liveData {
        emit(Async.Loading)
        try {
            val response = apiService.getAllStory("Bearer $token", size = 150, location = 1)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Async.Success(response.body()?.listStory ?: listOf()))
                } ?: emit(Async.Error("No Data"))
            } else {
                response.errorBody()?.let {
                    val obj = JSONObject(it.string())
                    emit(Async.Error(obj.getString("message")))
                }
            }
        } catch (e: Exception) {
            emit(Async.Error("$e, check your internet connection"))
        }
    }
}