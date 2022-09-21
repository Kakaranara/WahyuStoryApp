package com.example.wahyustoryapp.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Story::class], version = 1)
abstract class StoryRoomDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao

    companion object {
        @Volatile
        private var INSTANCE: StoryRoomDatabase? = null

        @JvmStatic
        fun getInstance(application: Application): StoryRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    application, StoryRoomDatabase::class.java, "story-db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}