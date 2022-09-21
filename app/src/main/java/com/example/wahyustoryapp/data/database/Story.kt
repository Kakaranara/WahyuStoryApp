package com.example.wahyustoryapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Story(
    @PrimaryKey(autoGenerate = true)
    val idOrder: Int,

    @ColumnInfo
    val id: String,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val description: String,

    @ColumnInfo
    val photoUrl: String,

    @ColumnInfo
    val lat: Double? = null,

    @ColumnInfo
    val long: Double? = null
)