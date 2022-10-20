package com.example.wahyustoryapp.data.repository

import com.example.wahyustoryapp.data.database.StoryDao
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.data.network.response.NormalResponse
import com.example.wahyustoryapp.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class StoryRepository(
    private val dao: StoryDao,
    private var service: ApiService,
    private var token: String
) : StoryRepositoryModel {

    override fun getStoryData() = dao.getAllStories()

    override suspend fun refreshRepositoryData(
        page: Int?,
        size: Int?,
        withLocation: Boolean
    ) {
        val location = when (withLocation) {
            false -> 0
            true -> 1
        }
        withContext(Dispatchers.IO) {
            val networkData = service
                .getAllStory(
                    "Bearer $token",
                    page = page,
                    size = size,
                    location = location
                )
            if (networkData.isSuccessful) {
                dao.deleteAll()
                networkData.body()?.let { response ->
                    val result = response.listStory.toEntity()
                    dao.insertAll(result)
                }
            }
        }
    }

    override suspend fun addStory(
        file: File,
        description: String
    ): Response<NormalResponse> {

        val requestDesc = description.toRequestBody("text/plain".toMediaType())
        val requestImage = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imgPart = MultipartBody.Part.createFormData("photo", file.name, requestImage)

        return service.uploadImage(
            "Bearer $token",
            imgPart, requestDesc
        )
    }

    override suspend fun clearDb() {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }

    companion object {
        private const val TAG = "STORY_REPOSITORY"
    }

}