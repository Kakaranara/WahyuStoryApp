package com.example.wahyustoryapp.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.wahyustoryapp.MainDispatcherRule
import com.example.wahyustoryapp.data.network.response.LoginResponse
import com.example.wahyustoryapp.data.repository.AuthRepository
import com.example.wahyustoryapp.getOrAwaitValue
import com.example.wahyustoryapp.helper.Async
import com.example.wahyustoryapp.ui.auth.AuthDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val disp = MainDispatcherRule()

    @Mock
    private lateinit var repository: AuthRepository
    private lateinit var viewModel: LoginViewModel


    @Before
    fun setup() {
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `if login success then return Success`() {
        val expectedLiveData = MutableLiveData<Async<LoginResponse>>()
        expectedLiveData.value = Async.Success(AuthDummy.provideLoginResponse())

        val expected = expectedLiveData.getOrAwaitValue()

        `when`(repository.login(AuthDummy.provideLoginForm())).thenReturn(expectedLiveData)
        val actual = viewModel.loginEvent(AuthDummy.provideLoginForm()).getOrAwaitValue()

        Mockito.verify(repository).login(AuthDummy.provideLoginForm())
        assertNotNull(actual)
        assertTrue(actual is Async.Success)
        assertEquals(expected, actual)
    }

    @Test
    fun `if network error then return Async Error`() {
        val expectedLiveData = MutableLiveData<Async<LoginResponse>>(Async.Error("Dummy"))
        `when`(repository.login(AuthDummy.provideLoginForm())).thenReturn(expectedLiveData)

        val actual = viewModel.loginEvent(AuthDummy.provideLoginForm()).getOrAwaitValue()
        Mockito.verify(repository).login(AuthDummy.provideLoginForm())
        assertTrue(actual is Async.Error)
        assertNotNull(actual)
    }


}