package com.example.wahyustoryapp.ui.main.addStory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wahyustoryapp.R
import com.example.wahyustoryapp.databinding.FragmentAddStoryBinding


class AddStoryFragment : Fragment(R.layout.fragment_add_story) {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar3.setupWithNavController(findNavController())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}