package com.example.wahyustoryapp.ui.main.addStory

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.wahyustoryapp.*
import com.example.wahyustoryapp.data.repository.StoryRepository
import com.example.wahyustoryapp.helper.Async
import com.example.wahyustoryapp.utils.FileUtilityProcessing
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {

    @get:Rule
    val task = InstantTaskExecutorRule()

    @get:Rule
    val disp = MainDispatcherRule()

    @Mock
    private lateinit var repository: StoryRepository
    private lateinit var viewModel: AddStoryViewModel

    @Before
    fun setup() {
        viewModel = AddStoryViewModel(repository, UnconfinedTestDispatcher())
    }

    @Test
    fun `posting live data should return success when invoking upload to Server`() = runTest {
        val file: File = mock(File::class.java)
        val desc = ""
        val latLng = LatLng(1.0, 1.0)
        val responseDummy = Response.success(DataDummy.provideNormalResponse())
        `when`(repository.addStory(file, desc, latLng)).thenReturn(responseDummy)

        viewModel.uploadToServer(file, desc, latLng)
        val actual = viewModel.posting.getOrAwaitValue()
        assertTrue(actual is Async.Success)
        verify(repository).addStory(file, desc, latLng)
        verify(repository).refreshRepositoryData()
    }

    @Test
    fun `when processing gallery file invoked, then photo and files should have been set and compress is false`() =
        runTest {
            val file = mock(File::class.java)
            val bitmap = mock(Bitmap::class.java)
            val utilityTest = mock(FileUtilityProcessing::class.java)
            mockStatic(BitmapFactory::class.java)
            `when`(BitmapFactory.decodeFile(file.path)).thenReturn(bitmap)
            `when`(utilityTest.reduceFileImage(file)).thenReturn(file)

            viewModel.processGalleryFile(file, utilityTest)
            val compress = viewModel.isCompressing.getOrAwaitValue()
            val photo = viewModel.photo.getOrAwaitValue()
            val files = viewModel.file.getOrAwaitValue()

            assertFalse(compress)
            assertNotNull(photo)
            assertNotNull(files)
        }

    @Test
    fun `when process camera file invoked, compressing should be false and file live data is set`() =
        runTest {
            val bitmap = mock(Bitmap::class.java)
            val file = mock(File::class.java)
            val application = mock(Application::class.java)
            val utilTest = mock(FileUtilityProcessing::class.java)

            `when`(utilTest.reduceFileImage(bitmap, application)).thenReturn(file)
            viewModel.processCameraFileFromBitmap(bitmap, application, utilTest)

            val compress = viewModel.isCompressing.getOrAwaitValue()
            val files = viewModel.file.getOrAwaitValue()
            val photo = viewModel.file.getOrAwaitValue()
            assertNotNull(files)
            assertNotNull(photo)
            assertFalse(compress)
        }
}