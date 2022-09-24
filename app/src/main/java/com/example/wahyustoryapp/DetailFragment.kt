package com.example.wahyustoryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.wahyustoryapp.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    val binding get() = _binding!!

    val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = args.dataStory

        binding.apply {
            detailName.text = arguments.name
            detailDate.text = arguments.createdAt
            detailDesc.text = arguments.description
            Glide.with(requireActivity())
                .load(arguments.photoUrl)
                .into(detailImage)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}