package com.example.wahyustoryapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.wahyustoryapp.MainDispatcherRule
import com.example.wahyustoryapp.data.fake.FakeApiStory
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.helper.Async
import com.example.wahyustoryapp.observeForTesting
import com.example.wahyustoryapp.preferences.AuthPreference
import com.example.wahyustoryapp.ui.auth.AuthDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {

    @get:Rule
    val task = InstantTaskExecutorRule()

    @get:Rule
    val disp = MainDispatcherRule()

    @Mock
    private lateinit var prefs: AuthPreference
    private lateinit var api: ApiService
    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        api = FakeApiStory()
        repository = AuthRepository(api, prefs)
    }

    @Test
    fun `Success login should return Success and invoke auth preferences`() = runTest {
        val dummyForm = AuthDummy.provideLoginForm()
        val dummyResponse = AuthDummy.provideLoginResponse()
        val actual = repository.login(dummyForm)

        actual.observeForTesting {
            Assert.assertTrue(actual.value is Async.Success)
            Mockito.verify(prefs).login()
            Mockito.verify(prefs).writeToken(dummyResponse.loginResult.token)
        }
    }
}