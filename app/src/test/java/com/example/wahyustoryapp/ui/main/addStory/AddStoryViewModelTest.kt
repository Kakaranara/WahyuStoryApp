package com.example.wahyustoryapp.ui.main.addStory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.wahyustoryapp.DataDummy
import com.example.wahyustoryapp.MainDispatcherRule
import com.example.wahyustoryapp.data.repository.StoryRepository
import com.example.wahyustoryapp.getOrAwaitValue
import com.example.wahyustoryapp.helper.Async
import com.example.wahyustoryapp.observeForTesting
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest{

    @get:Rule
    val task = InstantTaskExecutorRule()

    @get:Rule
    val disp = MainDispatcherRule()

    @Mock
    private lateinit var repository: StoryRepository
    private lateinit var viewModel : AddStoryViewModel

    @Before
    fun setup(){
        viewModel = AddStoryViewModel(repository)
    }

    @Test
    fun `posting live data should return success when invoking upload to Server`() = runTest{
        val file : File = mock(File::class.java)
        val desc = ""
        val latLng = LatLng(1.0,1.0)
        val responseDummy = Response.success(DataDummy.provideNormalResponse())
        `when`(repository.addStory(file, desc, latLng)).thenReturn(responseDummy)

        viewModel.uploadToServer(file, desc, latLng)
        val actual = viewModel.posting.getOrAwaitValue()
        assertTrue(actual is Async.Success)
        Mockito.verify(repository).addStory(file, desc, latLng)
        Mockito.verify(repository).refreshRepositoryData()
    }
}