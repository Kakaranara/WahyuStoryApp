package com.example.wahyustoryapp

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

//? Variable Extension
val Context.authDataStore : DataStore<Preferences> by preferencesDataStore(name = "auth")


//? Function Extension
fun View.showLoading(activity : Activity, isLoading: Boolean){
    if(isLoading){
        this.isEnabled = false
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }else{
        this.isEnabled = true
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}