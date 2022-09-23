@file:Suppress("DEPRECATION")

package com.example.wahyustoryapp.helper

import android.app.Activity
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager

object MySystem {
    fun hideSystemUI(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}