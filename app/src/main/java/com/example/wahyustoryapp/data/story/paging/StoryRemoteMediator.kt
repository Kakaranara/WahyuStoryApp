package com.example.wahyustoryapp.data.story.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.wahyustoryapp.data.database.RemoteKeys
import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.data.database.StoryRoomDatabase
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.toEntity


@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val remoteDataSource: ApiService,
    private val localDataSource: StoryRoomDatabase,
    private val token: String
) : RemoteMediator<Int, Story>() {

    companion object {
        const val INITIAL_PAGE = 1
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> INITIAL_PAGE
            LoadType.PREPEND -> return MediatorResult.Success(true)
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKeys(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(false)
                nextKey
            }
        }

        try {
            val response = remoteDataSource.getAllStory("Bearer $token", page)
            val body = response.body()?.listStory
            val isPaginationEnded = body?.isEmpty() ?: true
            val entity = body?.toEntity() ?: return MediatorResult.Success(true)

            localDataSource.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localDataSource.remoteKeysDao().deleteRemoteKeys()
                    localDataSource.storyDao().deleteAll()
                }
                val nextKey = if (isPaginationEnded) null else page + 1
                val keys = entity.map {
                    RemoteKeys(id = it.id, nextKey = nextKey)
                }
                localDataSource.remoteKeysDao().inserAll(keys)
                localDataSource.storyDao().insertAll(entity)
            }

            return MediatorResult.Success(false)

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getLastRemoteKeys(state: PagingState<Int, Story>): RemoteKeys? {
        return state.pages.lastOrNull()?.data?.lastOrNull()?.let {
            localDataSource.remoteKeysDao().getRemoteKeys(it.id)
        }
    }
}