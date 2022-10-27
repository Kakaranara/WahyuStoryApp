package com.example.wahyustoryapp.data.fake

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.wahyustoryapp.DataDummy
import com.example.wahyustoryapp.data.database.Story

class DummyPagingSource : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        val response = DataDummy.provideStoryDataInDatabase()
        return LoadResult.Page(response, null, null)
    }
}