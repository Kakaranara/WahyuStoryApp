package com.example.wahyustoryapp.data.fake

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingSource
import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.data.database.StoryDao

class FakeDaoStory : StoryDao {

    private val list = mutableListOf<Story>()
    private val storyLiveData = MutableLiveData<List<Story>>()

    override fun getAllStories(): PagingSource<Int, Story> {
        //TODO CHGANGE LATER
        return "k" as PagingSource<Int, Story>
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