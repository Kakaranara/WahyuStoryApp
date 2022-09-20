package com.example.wahyustoryapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.wahyustoryapp.R
import com.example.wahyustoryapp.authDataStore
import com.example.wahyustoryapp.data.auth.AuthPreference
import com.example.wahyustoryapp.data.auth.AuthViewModel
import com.example.wahyustoryapp.data.auth.AuthViewModelFactory
import com.example.wahyustoryapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login), View.OnClickListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!! //dari dokumentasinya begini, memakai double bang
    //( menghindari memory leaks )

    private lateinit var authPreference: AuthPreference
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory.getInstance(
            AuthPreference.getInstance(requireActivity().authDataStore)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authPreference = AuthPreference.getInstance(requireActivity().authDataStore)
        lifecycleScope.launch {
            authPreference.getToken().collect { values ->
                binding.debugToken2.text = values
            }
        }

        binding.btnLogin.setOnClickListener(this)
        binding.btnToRegister.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            binding.btnLogin -> {
                viewModel.login()
                viewModel.isLoginSuccess.observe(requireActivity()) {
                    if (it) {
                        val toHome = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(toHome)
                    } else {
                        Toast.makeText(requireActivity(), "gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            binding.btnToRegister -> {
                val toRegister = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(toRegister)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}