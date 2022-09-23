package com.example.wahyustoryapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.wahyustoryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



//        val navHost = supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
//        val navGraph: NavGraph = navHost.navController.navInflater.inflate(R.navigation.main_nav)
//        val authPrefs = AuthPreference.getInstance(authDataStore)
//
//        val job = Job()
//        val myScope = CoroutineScope(Dispatchers.Main + job)
//
//        //kode ini rentan terhadap configuration change
//        //mohon sarannya untuk membuat start destionation graph dengan lebih baik
//        myScope.launch(Dispatchers.Main) {
//            //checking login async
//            authPrefs.isLogin().collect {
//                if (it) {
//                    navGraph.setStartDestination(R.id.homeFragment)
//                } else {
//                    navGraph.setStartDestination(R.id.loginFragment)
//                }
//                //set navGraph with determined start destination
//                navHost.navController.graph = navGraph
//            }
//        }
    }
}