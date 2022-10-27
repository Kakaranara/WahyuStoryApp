package com.example.wahyustoryapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.wahyustoryapp.MainDispatcherRule
import com.example.wahyustoryapp.data.database.StoryRoomDatabase
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
class StoryRepositoryTest {

    @get:Rule
    val task = InstantTaskExecutorRule()

    @get:Rule
    val disp = MainDispatcherRule()

    @Mock
    private lateinit var database: StoryRoomDatabase

    @Mock
    private lateinit var api: ApiService

    private lateinit var storyRepository: StoryRepository
    private val token = ""

    @Before
    fun setup() {
        storyRepository = StoryRepository(database, api, token)
    }

//    @Test
//    fun `test`(){
//        storyRepository.getStoryData().getOrAwaitValue()
//    }
}