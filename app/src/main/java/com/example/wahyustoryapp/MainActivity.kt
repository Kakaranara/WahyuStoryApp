package com.example.wahyustoryapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.wahyustoryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    /**
     * This project use single-activity architecture.
     * Check the res > nav
     * This project also use offline-online pattern.
     * Making this project available when offline.
     */

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}