package com.example.wahyustoryapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.RoomDatabase
import com.example.wahyustoryapp.MainDispatcherRule
import com.example.wahyustoryapp.data.database.StoryDao
import com.example.wahyustoryapp.data.database.StoryRoomDatabase
import com.example.wahyustoryapp.data.fake.FakeApiStory
import com.example.wahyustoryapp.data.network.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

//@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class StoryRepositoryTest {

    @get:Rule
    val task = InstantTaskExecutorRule()

    @get:Rule
    val disp = MainDispatcherRule()

    @Mock
    private lateinit var dao : StoryDao

    @Mock
    private lateinit var database: StoryRoomDatabase
    private lateinit var api: ApiService
    private val token = ""
    private lateinit var repository: StoryRepository

    @Mock
    private lateinit var repoMock : StoryRepository

    @Before
    fun setup() {
        api = FakeApiStory()
        repository = StoryRepository(database, api, token)
    }

//    @Test
//    fun `test clearing db`() = runTest{
//        `when`(dao.deleteAll())
//        repository.clearDb()
//    }
}