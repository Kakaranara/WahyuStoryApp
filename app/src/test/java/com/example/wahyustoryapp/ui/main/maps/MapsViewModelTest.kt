package com.example.wahyustoryapp.ui.main.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.wahyustoryapp.DataDummy
import com.example.wahyustoryapp.data.network.response.ListStoryItem
import com.example.wahyustoryapp.data.repository.MapsRepository
import com.example.wahyustoryapp.getOrAwaitValue
import com.example.wahyustoryapp.helper.Async
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    val instantTask = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: MapsRepository
    private lateinit var viewModel: MapsViewModel

    @Before
    fun setup() {
        viewModel = MapsViewModel(repository)
    }

    @Test
    fun `data should return the api with a location`() {
        val expected = DataDummy.abc()
        val liveData = MutableLiveData<Async<List<ListStoryItem>>>()
        liveData.value = Async.Success(expected)
        `when`(repository.requestApisLocation()).thenReturn(liveData)

        val actual = viewModel.data().getOrAwaitValue()
        assertTrue(actual is Async.Success)

        if(actual is Async.Success){
            assertNotNull(actual.data[0].lon)
            assertNotNull(actual.data[0].lat)
        }
    }

}