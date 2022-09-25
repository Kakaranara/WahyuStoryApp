package com.example.wahyustoryapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.wahyustoryapp.databinding.ActivityMainBinding
import com.example.wahyustoryapp.helper.MySystem

class MainActivity : AppCompatActivity() {

    /**
     * This project use single-activity architecture.
     * Check the res > nav
     * This project also use offline-online pattern.
     * Making this project available when offline.
     */

    //---------------------------------------------------------------------------

    /**
     * Penentuan/validasi pertama kali masuk navigasi
     * Terdapat di login fragment
     * Pada method onCreate()
     */

    //!! PENTING
    /**
     * Aplikasijuga tersedia dalam bentuk landscape :D
     * Meski hanya di home;
     */

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        MySystem.hideSystemUI(this)
    }
}