package com.koniukhov.cinecircle.feature.search.ui.viewmodel

import app.cash.turbine.test
import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.usecase.GetMovieGenresUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeriesGenresUseCase
import com.koniukhov.cinecircle.feature.search.model.MovieFilterParams
import com.koniukhov.cinecircle.feature.search.model.TvSeriesFilterParams
import com.koniukhov.cinecircle.feature.search.repository.FilteredMediaRepository
import com.koniukhov.cinecircle.feature.search.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchRepository: SearchRepository
    private lateinit var filteredMediaRepository: FilteredMediaRepository
    private lateinit var movieGenresUseCase: GetMovieGenresUseCase
    private lateinit var tvSeriesGenresUseCase: GetTvSeriesGenresUseCase

    private val testDispatcher = StandardTestDispatcher()
    private val languageCode = "en"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        searchRepository = mockk(relaxed = true)
        filteredMediaRepository = mockk(relaxed = true)
        movieGenresUseCase = mockk()
        tvSeriesGenresUseCase = mockk()

        coEvery { movieGenresUseCase(any()) } returns emptyList()
        coEvery { tvSeriesGenresUseCase(any()) } returns emptyList()

        viewModel = SearchViewModel(
            searchRepository = searchRepository,
            filteredMediaRepository = filteredMediaRepository,
            movieGenresUseCase = movieGenresUseCase,
            tvSeriesGenresUseCase = tvSeriesGenresUseCase,
            languageCode = languageCode
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load movie and tv series genres`() = runTest(testDispatcher) {
        val movieGenres = listOf(
            Genre(id = 28, name = "Action"),
            Genre(id = 35, name = "Comedy")
        )
        val tvGenres = listOf(
            Genre(id = 18, name = "Drama"),
            Genre(id = 10765, name = "Sci-Fi & Fantasy")
        )

        coEvery { movieGenresUseCase(languageCode) } returns movieGenres
        coEvery { tvSeriesGenresUseCase(languageCode) } returns tvGenres

        val newViewModel = SearchViewModel(
            searchRepository = searchRepository,
            filteredMediaRepository = filteredMediaRepository,
            movieGenresUseCase = movieGenresUseCase,
            tvSeriesGenresUseCase = tvSeriesGenresUseCase,
            languageCode = languageCode
        )
        advanceUntilIdle()

        newViewModel.movieGenres.test {
            val genres = awaitItem()
            assertEquals(2, genres.size)
            assertEquals("Action", genres[28])
            assertEquals("Comedy", genres[35])
        }

        newViewModel.tvSeriesGenres.test {
            val genres = awaitItem()
            assertEquals(2, genres.size)
            assertEquals("Drama", genres[18])
            assertEquals("Sci-Fi & Fantasy", genres[10765])
        }

        coVerify { movieGenresUseCase(languageCode) }
        coVerify { tvSeriesGenresUseCase(languageCode) }
    }

    @Test
    fun `selectMovieFilters should update movie filter params and reset tv series filters`() = runTest(testDispatcher) {
        val movieParams = MovieFilterParams(
            sortBy = "popularity.desc",
            year = 2024,
            withGenres = "28,12"
        )

        viewModel.selectMovieFilters(movieParams)
        advanceUntilIdle()

        viewModel.moviesFilterParamsState.test {
            val params = awaitItem()
            assertNotNull(params)
            assertEquals("popularity.desc", params?.sortBy)
            assertEquals(2024, params?.year)
            assertEquals("28,12", params?.withGenres)
        }

        viewModel.tvSeriesFilterParamsState.test {
            val params = awaitItem()
            assertNull(params)
        }
    }

    @Test
    fun `selectTvSeriesFilters should update tv series filter params and reset movie filters`() = runTest(testDispatcher) {
        val tvParams = TvSeriesFilterParams(
            sortBy = "vote_average.desc",
            year = 2023,
            withGenres = "18,10765",
            airDateGte = "2023-01-01"
        )

        viewModel.selectTvSeriesFilters(tvParams)
        advanceUntilIdle()

        viewModel.tvSeriesFilterParamsState.test {
            val params = awaitItem()
            assertNotNull(params)
            assertEquals("vote_average.desc", params?.sortBy)
            assertEquals(2023, params?.year)
            assertEquals("18,10765", params?.withGenres)
        }

        viewModel.moviesFilterParamsState.test {
            val params = awaitItem()
            assertNull(params)
        }
    }

    @Test
    fun `resetMovieFilters should clear movie filter params`() = runTest(testDispatcher) {
        val movieParams = MovieFilterParams(
            sortBy = "popularity.desc",
            year = 2024
        )

        viewModel.selectMovieFilters(movieParams)
        advanceUntilIdle()

        viewModel.resetMovieFilters()
        advanceUntilIdle()

        viewModel.moviesFilterParamsState.test {
            val params = awaitItem()
            assertNull(params)
        }
    }

    @Test
    fun `resetTvSeriesFilters should clear tv series filter params`() = runTest(testDispatcher) {
        val tvParams = TvSeriesFilterParams(
            sortBy = "vote_average.desc",
            airDateGte = "2023-01-01",
            year = 2023
        )

        viewModel.selectTvSeriesFilters(tvParams)
        advanceUntilIdle()

        viewModel.resetTvSeriesFilters()
        advanceUntilIdle()

        viewModel.tvSeriesFilterParamsState.test {
            val params = awaitItem()
            assertNull(params)
        }
    }

    @Test
    fun `languages and countries should be initialized`() {
        assertTrue(viewModel.languages.isNotEmpty())
        assertTrue(viewModel.countries.isNotEmpty())
    }

    @Test
    fun `selectMovieFilters with null should reset movie filters`() = runTest(testDispatcher) {
        viewModel.selectMovieFilters(null)
        advanceUntilIdle()

        viewModel.moviesFilterParamsState.test {
            val params = awaitItem()
            assertNull(params)
        }
    }

    @Test
    fun `selectTvSeriesFilters with null should reset tv series filters`() = runTest(testDispatcher) {
        viewModel.selectTvSeriesFilters(null)
        advanceUntilIdle()

        viewModel.tvSeriesFilterParamsState.test {
            val params = awaitItem()
            assertNull(params)
        }
    }
}