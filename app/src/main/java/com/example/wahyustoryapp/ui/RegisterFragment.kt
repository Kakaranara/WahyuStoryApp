package com.example.wahyustoryapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.wahyustoryapp.MainNavDirections
import com.example.wahyustoryapp.data.auth.register.RegisterViewModel
import com.example.wahyustoryapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!! //dari dokumentasinya begini, memakai double bang
    //( menghindari memory leaks )

    val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnToLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)

        viewModel.message.observe(viewLifecycleOwner){
            Toast.makeText(requireActivity(), "$it ???", Toast.LENGTH_SHORT).show()
        }

        viewModel.isRegisterSucces.observe(viewLifecycleOwner){ success ->
            if(success){
                Toast.makeText(requireActivity(), "berhasil membuat akun", Toast.LENGTH_SHORT).show()
                val action = RegisterFragmentDirections.actionGlobalLoginFragment2()
                findNavController().navigate(action)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(view: View) {
        when(view){
            binding.btnToLogin -> {
                val global = MainNavDirections.actionGlobalLoginFragment2()
                findNavController().navigate(global)
            }
            binding.btnRegister -> {
                viewModel.registerAccount("debug","degK@gmail.com","123456")
            }
        }
    }
}