package com.example.wahyustoryapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.wahyustoryapp.MainDispatcherRule
import com.example.wahyustoryapp.data.fake.FakeApiStory
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.helper.Async
import com.example.wahyustoryapp.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

@OptIn(ExperimentalCoroutinesApi::class)
class MapsRepositoryTest {

    @get:Rule
    val task = InstantTaskExecutorRule()

    @get:Rule
    val disp = MainDispatcherRule()

    @Mock
    private lateinit var apiService: ApiService
    private lateinit var repository: MapsRepository
    val token = ""

    @Before
    fun setup() {
        apiService = FakeApiStory()
        repository = MapsRepository("", apiService)
    }

    @Test
    fun `apis should return lat and lon with non null and return success `() = runTest {
        val actual = repository.requestApisLocation()

        actual.observeForTesting {
            assertTrue(actual.value is Async.Success)
            val value = actual.value as Async.Success
            assertNotNull(value.data[0].lat)
            assertNotNull(value.data[0].lon)
        }
    }
}