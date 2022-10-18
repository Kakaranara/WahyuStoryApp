package com.example.wahyustoryapp

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.data.network.response.ListStoryItem
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

//? Variable Extension
val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

//? Function Extension
fun View.showOverlayWhileLoading(
    activity: Activity,
    root: View,
    progressBar: ProgressBar,
    isLoading: Boolean
) {
    //user can't interact with window.flags

    val darkColor =
        ContextCompat.getColor(activity, R.color.md_theme_dark_onSurface)
    val lightColor =
        ContextCompat.getColor(activity, R.color.md_theme_light_background)
    if (isLoading) {
        this.isEnabled = false
        root.setBackgroundColor(darkColor)
        progressBar.visible()
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    } else {
        this.isEnabled = true
        root.setBackgroundColor(lightColor)
        progressBar.gone()
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}

fun View.showOverlayWhileLoadingRef(
    activity: Activity,
    root: View,
    progressBar: ProgressBar,
) {
    //user can't interact with window.flags

    val darkColor =
        ContextCompat.getColor(activity, R.color.md_theme_dark_onSurface)

    this.isEnabled = false
    root.setBackgroundColor(darkColor)
    progressBar.visible()
    activity.window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

}

fun View.hideOverlayWhileLoadingRef(
    activity: Activity,
    root: View,
    progressBar: ProgressBar,
) {
    //user can't interact with window.flags

    val lightColor =
        ContextCompat.getColor(activity, R.color.md_theme_light_background)

    this.isEnabled = true
    root.setBackgroundColor(lightColor)
    progressBar.gone()
    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}


fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.disabled() {
    this.isEnabled = false
}

fun View.enabled() {
    this.isEnabled = true
}

fun List<ListStoryItem>.toEntity(): List<Story> {
    return this.map {
        Story(
            id = it.id,
            name = it.name,
            description = it.description,
            photoUrl = it.photoUrl,
            createdAt = it.createdAt.formatDate(),
            lat = it.lat,
            lon = it.lon
        )
    }
}

fun File.decodeToBitmap(): Bitmap = BitmapFactory.decodeFile(this.path)

fun String.formatDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ms'Z'", Locale.US)
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = inputFormat.parse(this) as Date

    val result = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(date)

    return result
}