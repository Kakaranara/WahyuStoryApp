package com.example.wahyustoryapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Query("SELECT * from remote_keys WHERE id = :id ")
    suspend fun getRemoteKeys(id: String): RemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserAll(remoteKeys: List<RemoteKeys>)

    @Query("DELETE FROM remote_keys")
    fun deleteRemoteKeys()

}

