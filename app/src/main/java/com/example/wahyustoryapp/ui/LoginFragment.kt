package com.example.wahyustoryapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.wahyustoryapp.R
import com.example.wahyustoryapp.authDataStore
import com.example.wahyustoryapp.data.auth.AuthPreference
import com.example.wahyustoryapp.data.auth.AuthViewModel
import com.example.wahyustoryapp.data.auth.AuthViewModelFactory
import com.example.wahyustoryapp.data.retrofit.LoginForm
import com.example.wahyustoryapp.databinding.FragmentLoginBinding
import com.example.wahyustoryapp.showOverlayWhileLoading

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
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            activity?.let { activity ->

                binding.btnLogin.showOverlayWhileLoading(
                    activity,
                    binding.root,
                    binding.loginProgress,
                    isLoading
                )
            }
        }

        viewModel.message.observe(requireActivity()) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.isLoginSuccess.observe(requireActivity()) {
            if (it) {
                val toHome = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(toHome)
            }
        }

        binding.btnLogin.setOnClickListener(this)
        binding.btnToRegister.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            binding.btnLogin -> {
                val form = getLoginForm()
                viewModel.login(form)


            }
            binding.btnToRegister -> {
                val toRegister = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(toRegister)
            }
        }
    }

    private fun getLoginForm(): LoginForm {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        return LoginForm(email, password)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}