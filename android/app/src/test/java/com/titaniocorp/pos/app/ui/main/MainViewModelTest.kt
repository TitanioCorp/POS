package com.titaniocorp.pos.app.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.titaniocorp.pos.api.SearchMovieRequest
import com.titaniocorp.pos.util.CoroutinesTestRule
import com.titaniocorp.pos.util.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class MainViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(MovieRepository::class.java)
    private lateinit var viewModel: MainViewModel

    @Before
    fun init() {
        viewModel = MainViewModel(repository)
    }

    @Test
    fun empty() {
        viewModel.movies.observeForever(mock())
        viewModel.searchMovies("")
        verify(repository).searchMovies(SearchMovieRequest("", 1))
    }
}