package com.example.wahyustoryapp.data.fake

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.wahyustoryapp.data.database.RemoteKeysDao
import com.example.wahyustoryapp.data.database.StoryDao
import com.example.wahyustoryapp.data.database.StoryRoomDatabase

abstract class FakeDatabase : StoryRoomDatabase() {
    override fun storyDao(): StoryDao {
        return FakeDaoStory()
    }

    override fun remoteKeysDao(): RemoteKeysDao {
        TODO("Not yet implemented")
    }
}