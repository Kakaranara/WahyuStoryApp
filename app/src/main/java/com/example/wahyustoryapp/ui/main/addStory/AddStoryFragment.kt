package com.example.wahyustoryapp.ui.main.addStory

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wahyustoryapp.databinding.FragmentAddStoryBinding
import com.example.wahyustoryapp.ui.main.home.ApplicationFactory
import java.io.File


class AddStoryFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!

    private var file: File? = null

    //shared view model
    private val viewModel by activityViewModels<AddStoryViewModel> {
        ApplicationFactory(requireActivity().application)
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val go = AddStoryFragmentDirections.actionAddStoryFragmentToCameraFragment()
            findNavController().navigate(go)
        } else {
            Toast.makeText(requireActivity(), "Permission tidak diberikan", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        binding.btnCamera.setOnClickListener(this)
        binding.btnGallery.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)

        viewModel.photo.observe(viewLifecycleOwner) {
            binding.imageView.setImageBitmap(it)
        }

        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.file.observe(viewLifecycleOwner) {
            file = it
        }
    }

    private fun setupToolbar() {
        binding.toolbar3.setupWithNavController(findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

        //removing all shared view model
        requireActivity().viewModelStore.clear()
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnCamera -> {
                requestPermission.launch(Manifest.permission.CAMERA)
            }
            binding.btnGallery -> {

            }
            binding.btnUpload -> {
                val et = binding.etDesc.text.toString()
                val description = et.ifEmpty {
                    "Tidak ada deskripsi"
                }
                file?.let {
                    viewModel.uploadToServer(it, description)
                } ?: Toast.makeText(requireActivity(), "Please input the image", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}