package com.example.wahyustoryapp.data.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface StoryDao {
    @Query("SELECT * FROM story ORDER BY idOrder DESC")
    fun getAllStories() : List<Story>
}