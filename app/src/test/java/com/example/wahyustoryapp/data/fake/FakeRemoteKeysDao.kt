package com.example.wahyustoryapp.data.fake

import com.example.wahyustoryapp.data.database.RemoteKeys
import com.example.wahyustoryapp.data.database.RemoteKeysDao

class FakeRemoteKeysDao : RemoteKeysDao {
    val list = mutableListOf<RemoteKeys>()
    override suspend fun getRemoteKeys(id: String): RemoteKeys? {
        return list.find { it.id == id }
    }

    override suspend fun inserAll(remoteKeys: List<RemoteKeys>) {
        list.addAll(remoteKeys)
    }

    override fun deleteRemoteKeys() {
        list.clear()
    }
}