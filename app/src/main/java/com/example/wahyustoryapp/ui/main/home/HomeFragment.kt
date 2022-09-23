package com.example.wahyustoryapp.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wahyustoryapp.*
import com.example.wahyustoryapp.data.auth.AuthPreference
import com.example.wahyustoryapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!! //dari dokumentasinya begini, memakai double bang
    //( menghindari memory leaks)

    private val viewModel by viewModels<HomeViewModel> {
        ApplicationFactory(requireActivity().application)
    }

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

        binding.fab.setOnClickListener {
            val go = HomeFragmentDirections.actionHomeFragmentToAddStoryFragment()
            findNavController().navigate(go)
        }

        viewModel.story.observe(viewLifecycleOwner){
            binding.rvHome.adapter = HomeAdapter(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            if(it){
                binding.progressBar2.visible()
            }else{
                binding.progressBar2.gone()
            }
        }

        viewModel.isNetworkError.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(requireActivity(), "Network error", Toast.LENGTH_SHORT).show()
            }
        }

        binding.rvHome.setHasFixedSize(true)
        val manager = LinearLayoutManager(requireActivity())
        binding.rvHome.layoutManager = manager
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
                R.id.action_refresh -> {
                    viewModel.refreshDatabase()
                    true
                }
                R.id.action_search -> {
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

    fun refreshData(){
        viewModel.refreshDatabase()
    }

    companion object{
        const val TAG = "Home Fragment"
    }
}