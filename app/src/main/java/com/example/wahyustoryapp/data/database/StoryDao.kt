package com.example.wahyustoryapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Query("SELECT * FROM story ORDER BY idOrder ASC")
    fun getAllStories(): PagingSource<Int, Story>
//    fun getAllStories(): LiveData<List<Story>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<Story>)

    @Query("DELETE FROM story")
    fun deleteAll()

    @Query("SELECT * FROM story ORDER BY idOrder ASC")
    fun getAllStoriesValues(): List<Story>

}