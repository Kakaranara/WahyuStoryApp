package com.example.wahyustoryapp.ui.main.addStory

import android.Manifest
import android.app.Activity
import android.content.Intent
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
import com.example.wahyustoryapp.*
import com.example.wahyustoryapp.databinding.FragmentAddStoryBinding
import com.example.wahyustoryapp.di.Injection
import com.example.wahyustoryapp.helper.Async
import java.io.File


class AddStoryFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!

    private var file: File? = null

    //shared view model
    private val viewModel by activityViewModels<AddStoryViewModel> {
//        ApplicationFactory(requireActivity().application)
        ViewModelFactory(Injection.provideStoryRepository(requireActivity()))
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

    private var intentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                val myFile = uriToFile(it, requireActivity())
                viewModel.processGalleryFile(myFile)
            } ?: Toast.makeText(
                requireActivity(),
                "Tidak ada file yang dipilih",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        binding.btnCamera.setOnClickListener(this)
        binding.btnGallery.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)
        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.photo.observe(viewLifecycleOwner) {
            binding.imageView.setImageBitmap(it)
        }

        viewModel.posting.observe(viewLifecycleOwner) {
            when (it) {
                is Async.Success -> {
                    loadingEnds()
                    it.data.body()?.let {
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    findNavController().popBackStack()
                }
                is Async.Error -> {
                    loadingEnds()
                    Toast.makeText(requireActivity(), it.error, Toast.LENGTH_SHORT).show()
                }
                is Async.Loading -> {
                    showLoading()
                }
            }
        }

        viewModel.file.observe(viewLifecycleOwner) {
            file = it
        }

        viewModel.isCompressing.observe(viewLifecycleOwner) { compressing ->
            //compressing dilakukan dalam ranah IO
            when (compressing) {
                true -> {
                    showLoading()
                    Toast.makeText(
                        requireActivity(),
                        "Please wait (Compressing)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                false -> {
                    loadingEnds()
                    Toast.makeText(requireActivity(), "Compressing Complete", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    private fun showLoading() {
        binding.btnUpload.disabled()
        binding.btnUpload.text = ""
        binding.progressBar.visible()
    }

    private fun loadingEnds() {
        binding.progressBar.gone()
        binding.btnUpload.text = requireActivity().resources.getString(R.string.upload)
        binding.btnUpload.enabled()
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
                Intent(Intent.ACTION_GET_CONTENT).also {
                    it.type = "image/*"
                    val chooser = Intent.createChooser(it, "Choose a picture")
                    intentGallery.launch(chooser)
                }
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

}