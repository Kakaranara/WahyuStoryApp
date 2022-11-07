package com.example.wahyustoryapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.wahyustoryapp.DataDummy
import com.example.wahyustoryapp.MainDispatcherRule
import com.example.wahyustoryapp.constant.Constant
import com.example.wahyustoryapp.data.database.RemoteKeysDao
import com.example.wahyustoryapp.data.database.StoryDao
import com.example.wahyustoryapp.data.database.StoryRoomDatabase
import com.example.wahyustoryapp.data.fake.FakeApiStory
import com.example.wahyustoryapp.data.fake.FakeRemoteKeysDao
import com.example.wahyustoryapp.data.fake.FakeStoryDao
import com.example.wahyustoryapp.data.network.ApiService
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class StoryRepositoryTest {

    @get:Rule
    val task = InstantTaskExecutorRule()

    @get:Rule
    val disp = MainDispatcherRule()


    private lateinit var api: ApiService
    private lateinit var database: StoryRoomDatabase
    private lateinit var repository: StoryRepository
    private val token = ""
    private lateinit var dao: StoryDao
    private lateinit var remoteDao: RemoteKeysDao

    @Before
    fun setup() {
        database = mock(StoryRoomDatabase::class.java)
        api = FakeApiStory()
        dao = FakeStoryDao()
        remoteDao = FakeRemoteKeysDao()

        val testDispatcher = UnconfinedTestDispatcher()
        repository = StoryRepository(database, api, token, testDispatcher)

        // * hal ini harus dilakukan, karena bila tidak database akan mengeluarkan :
        // ! null pointer exception! invoke suspend!
        // ? saya juga kurang tahu sebenarnya kenapa ini bisa terjadi.
        // ? mungkin karena yang di mock abstract class?
        `when`(database.storyDao()).thenReturn(dao)
        `when`(database.remoteKeysDao()).thenReturn(remoteDao)
    }

    @Test
    fun `AFTER Repository invoke refreshDb, db size should be the same with constant SIZE_FOR_REFRESH`() =
        runTest {
            val dummy = DataDummy.provideStoryList()
            repeat(3) { //? just to simulate db operating
                database.storyDao().insertAll(dummy)
            }

            repository.refreshRepositoryData()
            Assert.assertNotNull(database.storyDao().getAllStoriesValues().size)
            Assert.assertEquals(
                database.storyDao().getAllStoriesValues().size,
                Constant.SIZE_FOR_REFRESH
            )
        }

    @Test
    fun `when latLng is null, api call uploadImage`() = runTest {
        val mockFile = mock(File::class.java)
        repository.addStory(mockFile, "", null)

        Assert.assertTrue((api as FakeApiStory).isUploadImageTriggered)
    }

    @Test
    fun `when latLng is not null, api call uploadImageWithLocation`() = runTest {
        val mockFile = mock(File::class.java)
        repository.addStory(mockFile, "", LatLng(1.0, 1.0))

        Assert.assertTrue((api as FakeApiStory).isUploadImageWithLocationTriggered)
    }

    @Test
    fun `clearing database from repository should make storyDao empty`() = runTest {
        val dummy = DataDummy.provideStoryList()
        database.storyDao().insertAll(dummy)
        repository.clearDb()

        Assert.assertTrue(database.storyDao().getAllStoriesValues().isEmpty())
    }
}