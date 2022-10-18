package com.example.wahyustoryapp.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.wahyustoryapp.MainNavDirections
import com.example.wahyustoryapp.data.network.RegisterForm
import com.example.wahyustoryapp.databinding.FragmentRegisterBinding
import com.example.wahyustoryapp.helper.Async
import com.example.wahyustoryapp.hideOverlayWhileLoadingRef
import com.example.wahyustoryapp.showOverlayWhileLoadingRef

class RegisterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!! //dari dokumentasinya begini, memakai double bang
    //( menghindari memory leaks )

    private val viewModel by viewModels<RegisterViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnToLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        setupObserver()
    }

    private fun setupObserver() {

        viewModel.registerEvent.observe(viewLifecycleOwner) { event ->
            activity?.let {
                when (event) {
                    is Async.Error -> {
                        binding.btnRegister.hideOverlayWhileLoadingRef(
                            it,
                            binding.root,
                            binding.registerProgressBar
                        )
                        Toast.makeText(it, event.error, Toast.LENGTH_SHORT).show()
                    }
                    is Async.Loading -> {
                        binding.btnRegister.showOverlayWhileLoadingRef(
                            it,
                            binding.root,
                            binding.registerProgressBar
                        )
                    }
                    is Async.Success -> {
                        binding.btnRegister.hideOverlayWhileLoadingRef(
                            it,
                            binding.root,
                            binding.registerProgressBar
                        )
                        Toast.makeText(it, event.data.message(), Toast.LENGTH_SHORT).show()
                        val action = RegisterFragmentDirections.actionGlobalLoginFragment2()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnToLogin -> {
                val global = MainNavDirections.actionGlobalLoginFragment2()
                findNavController().navigate(global)
            }
            binding.btnRegister -> {
                val etEmail = binding.etRegisterEmail
                val etPassword = binding.etRegisterPassword
                val etConfirmPass = binding.etRegisterConfirmPassword
                if (etEmail.error == null && etPassword.error == null) {
                    if (etPassword.text.toString() == etConfirmPass.text.toString()) {
                        val form = getRegisterForm()
                        viewModel.registerAccount(form)
                    } else {
                        etConfirmPass.error = "Password tidak sama"
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Harap baca ketentuan diatas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getRegisterForm(): RegisterForm {
        binding.apply {
            val name = etRegisterName.text.toString()
            val email = etRegisterEmail.text.toString()
            val password = etRegisterPassword.text.toString()
            return RegisterForm(name, email, password)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}