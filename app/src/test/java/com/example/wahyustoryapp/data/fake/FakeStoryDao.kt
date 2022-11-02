package com.example.wahyustoryapp.data.fake

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.data.database.StoryDao

class FakeStoryDao : StoryDao {

    private val list = mutableListOf<Story>()
    private val storyLiveData = MutableLiveData<List<Story>>()

    fun getList() = list

    override fun getAllStories(): PagingSource<Int, Story> {

        return DummyPagingSource()
//        return "k" as PagingSource<Int, Story>
    }

    override fun insertAll(data: List<Story>) {
        list.addAll(data)
        storyLiveData.postValue(list)
    }

    override fun deleteAll() {
        list.clear()
        storyLiveData.postValue(list)
    }

    override fun getAllStoriesValues(): List<Story> {
        return list
    }
}