package com.example.wahyustoryapp.ui.auth.register

import com.example.wahyustoryapp.data.repository.AuthRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class RegisterViewModelTest{


    @Mock
    private lateinit var repository : AuthRepository
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup(){
        viewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when`(){

    }

}