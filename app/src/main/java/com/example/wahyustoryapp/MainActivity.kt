package com.example.wahyustoryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavGraph
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import com.example.wahyustoryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var isLogin = false
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        val navGraph: NavGraph = navHost.navController.navInflater.inflate(R.navigation.main_nav)

        if (isLogin) {
            navGraph.setStartDestination(R.id.homeFragment)
        } else {
            navGraph.setStartDestination(R.id.loginFragment)
        }

        navHost.navController.graph = navGraph
    }
}