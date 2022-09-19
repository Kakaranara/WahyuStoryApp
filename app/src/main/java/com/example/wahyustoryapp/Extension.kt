package com.example.wahyustoryapp

import android.content.Context
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

//? Variable Extension
val Context.authDataStore : DataStore<Preferences> by preferencesDataStore(name = "auth")


//? Function Extension
