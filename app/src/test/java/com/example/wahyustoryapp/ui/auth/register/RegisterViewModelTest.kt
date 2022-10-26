package com.example.wahyustoryapp.ui.auth.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.wahyustoryapp.data.network.response.NormalResponse
import com.example.wahyustoryapp.data.repository.AuthRepository
import com.example.wahyustoryapp.getOrAwaitValue
import com.example.wahyustoryapp.helper.Async
import com.example.wahyustoryapp.ui.auth.AuthDummy
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: AuthRepository
    private lateinit var viewModel: RegisterViewModel
    val successRegisterResponse = AuthDummy.provideRegisterResponse()
    val registerForm = AuthDummy.provideRegisterForm()

    @Before
    fun setup() {
        viewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when register called from repository it should return Success and not null`() {
        val expected = MutableLiveData<Async<NormalResponse>>()
        expected.value = Async.Success(successRegisterResponse)
        `when`(repository.register(registerForm)).thenReturn(expected)

        val actual = viewModel.registerAccount(registerForm).getOrAwaitValue()
        Assert.assertTrue(actual is Async.Success)
        Assert.assertNotNull(actual)
    }

    @Test
    fun `when register failed it should return Error and also not null`(){
        val expected = MutableLiveData<Async<NormalResponse>>()
        expected.value = Async.Error("Something Error")
        `when`(repository.register(registerForm)).thenReturn(expected)

        val actual = viewModel.registerAccount(registerForm).getOrAwaitValue()
        Assert.assertTrue(actual is Async.Error)
        Assert.assertNotNull(actual)
    }

}