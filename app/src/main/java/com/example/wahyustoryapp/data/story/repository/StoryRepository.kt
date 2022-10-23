package com.example.wahyustoryapp.data.story.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.wahyustoryapp.data.database.StoryRoomDatabase
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.data.network.response.NormalResponse
import com.example.wahyustoryapp.data.story.paging.StoryRemoteMediator
import com.example.wahyustoryapp.toEntity
import com.google.android.gms.maps.model.LatLng
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
    private val database: StoryRoomDatabase,
    private var service: ApiService,
    private var token: String
) : StoryRepositoryModel {

    @OptIn(ExperimentalPagingApi::class)
    override fun getStoryData() = Pager(
        config = PagingConfig(
            pageSize = 1,
            initialLoadSize = 1,
            prefetchDistance = 0,

        ),
        remoteMediator = StoryRemoteMediator(service, database, token),
        pagingSourceFactory = {
            database.storyDao().getAllStories()
        }).liveData

    override suspend fun refreshRepositoryData(
        page: Int?, size: Int?, withLocation: Boolean
    ) {
        val location = when (withLocation) {
            false -> 0
            true -> 1
        }
        withContext(Dispatchers.IO) {
            val networkData = service.getAllStory(
                "Bearer $token", page = page, size = size, location = location
            )
            if (networkData.isSuccessful) {
                database.storyDao().deleteAll()
                networkData.body()?.let { response ->
                    val result = response.listStory.toEntity()
                    database.storyDao().insertAll(result)
                }
            }
        }
    }

    override suspend fun addStory(
        file: File, description: String, latLng: LatLng?
    ): Response<NormalResponse> {

        val lat = latLng?.latitude
        val lon = latLng?.longitude

        val requestDesc = description.toRequestBody("text/plain".toMediaType())
        val requestImage = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imgPart = MultipartBody.Part.createFormData("photo", file.name, requestImage)

        return if (lat != null && lon != null) {
            service.uploadImageWithLocation("Bearer $token", imgPart, requestDesc, lat, lon)
        } else {
            service.uploadImage("Bearer $token", imgPart, requestDesc)
        }

    }

    override suspend fun clearDb() {
        withContext(Dispatchers.IO) {
            database.storyDao().deleteAll()
        }
    }

    companion object {
        private const val TAG = "STORY_REPOSITORY"
    }

}