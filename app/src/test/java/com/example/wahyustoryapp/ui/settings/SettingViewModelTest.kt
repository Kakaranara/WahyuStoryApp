package com.example.wahyustoryapp.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.wahyustoryapp.MainDispatcherRule
import com.example.wahyustoryapp.getOrAwaitValue
import com.example.wahyustoryapp.preferences.SettingPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SettingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val disp = MainDispatcherRule()

    @Mock
    private lateinit var settingPreferences: SettingPreferences
    private lateinit var settingViewModel: SettingViewModel

    @Before
    fun setup() {
        settingViewModel = SettingViewModel(settingPreferences)
    }

    @Test
    fun `writeTheme should call preferences save Theme`() = runTest{
        val dummy = false
        settingViewModel.writeTheme(dummy)
        Mockito.verify(settingPreferences).saveThemeSetting(dummy)
    }

    @Test
    fun `when darkMode should return true and not null`(){
        val isDarkMode = true
        val la = flow<Boolean> { emit(isDarkMode) }
        `when`(settingPreferences.getThemeSettings()).thenReturn(la)
        val actual  = settingViewModel.getTheme().getOrAwaitValue()

        assertNotNull(actual)
        assertTrue(actual)
    }

    @Test
    fun `when lightMode should return true`(){
        val isDarkMode = false
        val la = flow<Boolean> { emit(isDarkMode) }
        `when`(settingPreferences.getThemeSettings()).thenReturn(la)
        val actual  = settingViewModel.getTheme().getOrAwaitValue()

        assertNotNull(actual)
        assertFalse(actual)
    }

}