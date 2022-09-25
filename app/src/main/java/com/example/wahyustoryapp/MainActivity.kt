package com.example.wahyustoryapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.wahyustoryapp.databinding.ActivityMainBinding
import com.example.wahyustoryapp.helper.MySystem
import com.example.wahyustoryapp.preferences.SettingPreferences
import kotlinx.coroutines.runBlocking

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

    val viewModel by viewModels<MainViewModel> {
        SettingsFactory.getInstance(SettingPreferences.getInstance(settingsDataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        MySystem.hideSystemUI(this)

        viewModel.getThemeSettings().value

        if(savedInstanceState == null){
            /**
             * Hal ini berguna agar user tidak melihat pergantian tema
             * Saat aplikasi baru pertama kali dijalankan
             * (Karena terlihat aneh)
             */
            runBlocking {
                val darkMode = viewModel.getSingleThemeSettings()
                setupDarkMode(darkMode)
            }
        }

        viewModel.getThemeSettings().observe(this) { darkMode ->
            setupDarkMode(darkMode)
        }
    }
    private fun setupDarkMode(isDarkMode: Boolean){
        when (isDarkMode) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}