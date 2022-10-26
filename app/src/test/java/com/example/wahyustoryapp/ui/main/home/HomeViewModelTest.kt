package com.example.wahyustoryapp.ui.main.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.wahyustoryapp.DataDummy
import com.example.wahyustoryapp.MainDispatcherRule
import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.data.repository.StoryRepository
import com.example.wahyustoryapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
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
import org.mockito.junit.MockitoJUnitRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val task = InstantTaskExecutorRule()

    @get:Rule
    val disp = MainDispatcherRule()

    @Mock
    private lateinit var repository: StoryRepository

    private val dummyStory = DataDummy.provideStoryDataInDatabase()
    private lateinit var viewModel: HomeViewModel


    @Before
    fun setup() {

        val data: PagingData<Story> = PagingData.from(dummyStory)
        val liveData: LiveData<PagingData<Story>> = MutableLiveData(data)
        `when`(repository.getStoryData()).thenReturn(liveData)

        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `Paging story data should return the correct data`() = runTest {

        val asyncDiffer = AsyncPagingDataDiffer(
            diffCallback = HomeAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        val actual = viewModel.story.getOrAwaitValue()
        asyncDiffer.submitData(actual)

        assertNotNull(asyncDiffer.snapshot())
        assertEquals(asyncDiffer.snapshot()[0]?.id, dummyStory[0].id)
        Mockito.verify(repository).getStoryData()
    }

    @Test
    fun `clearDatabase function should invoke repository clear database`() = runTest {
        viewModel.clearDatabase()
        Mockito.verify(repository).clearDb()
    }
}


val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}