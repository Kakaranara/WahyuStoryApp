package com.example.wahyustoryapp.data.fake

import com.example.wahyustoryapp.data.database.RemoteKeysDao
import com.example.wahyustoryapp.data.database.StoryDao
import com.example.wahyustoryapp.data.database.StoryRoomDatabase

abstract class FakeStoryDatabase : StoryRoomDatabase() {
    override fun storyDao(): StoryDao {
        return FakeStoryDao()
    }

    override fun remoteKeysDao(): RemoteKeysDao {
        return FakeRemoteKeysDao()
    }
}