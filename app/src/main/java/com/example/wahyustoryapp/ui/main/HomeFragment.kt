package com.example.wahyustoryapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.wahyustoryapp.MainNavDirections
import com.example.wahyustoryapp.R
import com.example.wahyustoryapp.authDataStore
import com.example.wahyustoryapp.data.auth.AuthPreference
import com.example.wahyustoryapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!! //dari dokumentasinya begini, memakai double bang
    //( menghindari memory leaks)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        val pref = AuthPreference.getInstance(requireActivity().authDataStore)
        lifecycleScope.launch {
            pref.getToken().collect{value ->
                binding.debugToken.text = value
            }
        }
    }

    private fun setupToolbar() {
        val navCont = findNavController()
        navCont.graph.setStartDestination(R.id.homeFragment)
        val appBarConfig = AppBarConfiguration(navCont.graph)
        binding.toolbar.setupWithNavController(navCont, appBarConfig)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_setting -> {
                    val go = HomeFragmentDirections.actionHomeFragmentToSettingFragment()
                    findNavController().navigate(go)
                    true
                }
                R.id.action_logout -> {
                    lifecycleScope.launch {
                        val pref = AuthPreference.getInstance(requireActivity().authDataStore)
                        pref.logout()
                        val go = MainNavDirections.actionGlobalLoginFragment2()
                        findNavController().navigate(go)
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val TAG = "Home Fragment"
    }
}