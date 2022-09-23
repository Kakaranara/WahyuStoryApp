package com.example.wahyustoryapp.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class Story(
    @PrimaryKey(autoGenerate = true)
    val idOrder: Int = 0,
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val lat: Double? = null,
    val lon: Double? = null
) : Parcelable