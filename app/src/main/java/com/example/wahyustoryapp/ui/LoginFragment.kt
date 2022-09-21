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
import com.example.wahyustoryapp.data.retrofit.LoginForm
import com.example.wahyustoryapp.databinding.FragmentLoginBinding
import com.example.wahyustoryapp.showOverlayWhileLoading
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

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            activity?.let { activity ->
//                binding.btnLogin.showLoading(it, binding.loginProgress, isLoading)

                binding.btnLogin.showOverlayWhileLoading(
                    activity,
                    binding.root,
                    binding.loginProgress,
                    isLoading
                )

//                val darkColor =
//                    ContextCompat.getColor(requireContext(), R.color.md_theme_dark_onSurface)
//                val lightColor =
//                    ContextCompat.getColor(requireContext(), R.color.md_theme_light_background)
//                if (isLoading) {
//                    binding.btnLogin.isEnabled = false
//                    binding.root.setBackgroundColor(darkColor)
//                    binding.loginProgress.visibility = View.VISIBLE
//                    activity.window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//                } else {
//                    binding.btnLogin.isEnabled = true
//                    binding.root.setBackgroundColor(lightColor)
//                    binding.loginProgress.visibility = View.GONE
//                    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//                }
            }
        }

//        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.md_theme_light_background))
        binding.btnLogin.setOnClickListener(this)
        binding.btnToRegister.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            binding.btnLogin -> {
                val form = getLoginForm()
                viewModel.login(form)

                viewModel.message.observe(this) {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                }
                viewModel.isLoginSuccess.observe(requireActivity()) {
                    if (it) {
                        val toHome = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(toHome)
                    }
                }
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