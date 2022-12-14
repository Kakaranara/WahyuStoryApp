package com.example.wahyustoryapp.ui.main.home.detail

import android.animation.ObjectAnimator
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.wahyustoryapp.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition =
            TransitionInflater.from(requireActivity()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }

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

        ObjectAnimator.ofFloat(binding.detailDesc, View.ALPHA, 1f).apply {
            duration = 500L
        }.start()

        binding.apply {
            detailName.text = arguments.name
            detailDate.text = arguments.createdAt
            detailDesc.text = arguments.description
            detailImage.apply {
                Glide.with(requireActivity())
                    .load(arguments.photoUrl)
                    .into(this)
            }
//            Glide.with(requireActivity())
//                .load(arguments.photoUrl)
//                .into(detailImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}