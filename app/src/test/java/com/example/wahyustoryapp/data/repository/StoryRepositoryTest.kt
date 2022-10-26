package com.example.wahyustoryapp.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class StoryRepositoryTest {

//    @get:Rule
//    val task = InstantTaskExecutorRule()
//
//    @get:Rule
//    val disp = MainDispatcherRule()
//
//    private lateinit var api: ApiService
//    private lateinit var dao: StoryDao
//    private lateinit var repository: StoryRepositoryModel
//
//    @Before
//    fun setup() {
//        //Collaborator
//        dao = FakeDaoStory()
//        api = FakeApiStory()
//
//        repository = StoryRepository(dao, api, "fake token") //SUT
//    }
//
//    @Test
//    fun `when database got refreshed then it comes to the first refreshed data`() = runTest {
//        val expected = DataDummy.provideStoryDataInDatabase(10)
//        repository.refreshRepositoryData(size = 10)
//        dao.insertAll(expected)        //? Simulates inserting any data
//
//        repository.refreshRepositoryData(size = 10)
//        val actual = repository.getStoryData().getOrAwaitValue()
//
//        Assert.assertNotNull(actual)
//        Assert.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `when delete all called db will be 0 item (empty)`() = runTest {
//        repository.refreshRepositoryData(null, null, false)
//        repository.clearDb()
//
//        val data = repository.getStoryData().getOrAwaitValue()
//        Assert.assertTrue(data.isEmpty())
//    }

}