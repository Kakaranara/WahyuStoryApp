package com.example.wahyustoryapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Story::class, RemoteKeys::class], version = 2, exportSchema = true)
abstract class StoryRoomDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryRoomDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): StoryRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, StoryRoomDatabase::class.java, "story-db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}