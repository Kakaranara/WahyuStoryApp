package com.example.wahyustoryapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.wahyustoryapp.MainDispatcherRule
import com.example.wahyustoryapp.data.database.StoryRoomDatabase
import com.example.wahyustoryapp.data.fake.FakeApiStory
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.getOrAwaitValue
import com.example.wahyustoryapp.ui.main.home.HomeAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@OptIn(ExperimentalCoroutinesApi::class)
class StoryRepositoryTest {

    @get:Rule
    val task = InstantTaskExecutorRule()

    @get:Rule
    val disp = MainDispatcherRule()

    private lateinit var repository: StoryRepository
    private lateinit var database: StoryRoomDatabase
    private lateinit var api: ApiService

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            StoryRoomDatabase::class.java
        ).allowMainThreadQueries().build()
        api = FakeApiStory()
        repository = StoryRepository(database, api, "")
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun test() = runTest {
        val a = repository.getStoryData().getOrAwaitValue()
        val asyncDiffer = AsyncPagingDataDiffer(
            diffCallback = HomeAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        asyncDiffer.submitData(a)

        assertNotNull(asyncDiffer.snapshot())
    }

}


val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}