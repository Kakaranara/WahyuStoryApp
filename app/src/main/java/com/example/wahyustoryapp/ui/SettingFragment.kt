package com.example.wahyustoryapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.wahyustoryapp.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!! //dari dokumentasinya begini, memakai double bang
    //( menghindari memory leaks)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        binding.btnChangeLanguage.setOnClickListener{
            Intent(Settings.ACTION_LOCALE_SETTINGS).also {
                startActivity(it)
            }
        }
    }

    private fun setupToolbar(){
        val navController = findNavController()
        val appBarConfig = AppBarConfiguration(navController.graph)
        binding.toolbar2.setupWithNavController(navController, appBarConfig)
    }
}