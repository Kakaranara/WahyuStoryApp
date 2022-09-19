package com.example.wahyustoryapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.wahyustoryapp.MainNavDirections
import com.example.wahyustoryapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!! //dari dokumentasinya begini, memakai double bang
    //( menghindari memory leaks )

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

        binding.btnToLogin.setOnClickListener {
            val global = MainNavDirections.actionGlobalLoginFragment2()
            findNavController().navigate(global)
//            val toLogin = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
//            findNavController().navigate(toLogin)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}