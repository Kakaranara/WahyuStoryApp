package com.example.wahyustoryapp.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.example.wahyustoryapp.preferences.AuthPreference
import com.example.wahyustoryapp.data.auth.AuthViewModelFactory
import com.example.wahyustoryapp.data.auth.LoginViewModel
import com.example.wahyustoryapp.data.network.LoginForm
import com.example.wahyustoryapp.databinding.FragmentLoginBinding
import com.example.wahyustoryapp.showOverlayWhileLoading
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class LoginFragment : Fragment(R.layout.fragment_login), View.OnClickListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!! //dari dokumentasinya begini, memakai double bang
    //( menghindari memory leaks )

    /**
     * Validasi dilakukan di halaman login
     * Cek pada method OnCreate()
     */

    private val viewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(
            AuthPreference.getInstance(requireActivity().authDataStore)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener(this)
        binding.btnToRegister.setOnClickListener(this)
        setupObserver()
        setupAnimation()
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.loginTitle, View.ALPHA, 1f).apply {
            duration = 4000L
        }.start()

        ObjectAnimator.ofFloat(binding.loginTitle, View.TRANSLATION_X, 0f, 60f).apply{
            duration = 5000
            repeatCount =ObjectAnimator.INFINITE
            repeatMode =ObjectAnimator.REVERSE
        }.start()

        val email = ObjectAnimator.ofFloat(binding.emailLayout, View.ALPHA, 1f).setDuration(1000)
        val password = ObjectAnimator.ofFloat(binding.passwordLayout, View.ALPHA, 1f).setDuration(1000)
        val button = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(1000L)
        val dontHaveAcc = ObjectAnimator.ofFloat(binding.tvHaveAccount, View.ALPHA, 1f).setDuration(1000)
        val toRegister = ObjectAnimator.ofFloat(binding.btnToRegister, View.ALPHA, 1f).setDuration(1000)

        val authAnim = AnimatorSet().apply {
            playTogether(button, dontHaveAcc, toRegister)
        }

        AnimatorSet().apply {
            playSequentially(email, password, authAnim)
        }.start()
    }

    private fun setupObserver() {
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

        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.isLoginSuccess.observe(viewLifecycleOwner) {
            if (it) {
                val toHome = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(toHome)
            }
        }
    }

    override fun onClick(v: View) {
        when (v) {
            binding.btnLogin -> {
                val etEmail = binding.etEmail
                val etPassword = binding.etPassword
                val emailLayout = binding.emailLayout
                val passwordLayout = binding.passwordLayout

                if (etEmail.error == null && etPassword.error == null) {
                    emailLayout.error = null
                    passwordLayout.error = null
                    val form = getLoginForm()
                    viewModel.login(form)
                } else {
                    emailLayout.error = etEmail.error
                    passwordLayout.error = etPassword.error
                    Toast.makeText(
                        requireActivity(),
                        "harap baca ketentuan diatas",
                        Toast.LENGTH_SHORT
                    ).show()
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = AuthPreference.getInstance(requireActivity().authDataStore)
        //sengaja menggunakan runblocking
        //agar dapat dicek dahulu sebelum view dibuat
        runBlocking {
            val isLogin = prefs.isLogin().first()
            if (isLogin) {
                val goto = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(goto)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}