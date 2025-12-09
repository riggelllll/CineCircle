package com.koniukhov.cinecircle.feature.home.ui.viewmodel

import app.cash.turbine.test
import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.usecase.GetAiringTodayTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetMovieGenresUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetNowPlayingMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetOnAirTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetPopularMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetPopularTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTopRatedMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTopRatedTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTrendingMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTrendingTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeriesGenresUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetUpcomingMoviesUseCase
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var getMovieGenresUseCase: GetMovieGenresUseCase
    private lateinit var getTvSeriesGenresUseCase: GetTvSeriesGenresUseCase
    private lateinit var getTrendingMoviesUseCase: GetTrendingMoviesUseCase
    private lateinit var getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase
    private lateinit var getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase
    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
    private lateinit var getAiringTodayTvSeriesUseCase: GetAiringTodayTvSeriesUseCase
    private lateinit var getOnAirTvSeriesUseCase: GetOnAirTvSeriesUseCase
    private lateinit var getTrendingTvSeriesUseCase: GetTrendingTvSeriesUseCase
    private lateinit var getPopularTvSeriesUseCase: GetPopularTvSeriesUseCase
    private lateinit var getTopRatedTvSeriesUseCase: GetTopRatedTvSeriesUseCase

    private val testDispatcher = StandardTestDispatcher()
    private val languageCode = "en-US"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getMovieGenresUseCase = mockk()
        getTvSeriesGenresUseCase = mockk()
        getTrendingMoviesUseCase = mockk()
        getNowPlayingMoviesUseCase = mockk()
        getPopularMoviesUseCase = mockk()
        getTopRatedMoviesUseCase = mockk()
        getUpcomingMoviesUseCase = mockk()
        getAiringTodayTvSeriesUseCase = mockk()
        getOnAirTvSeriesUseCase = mockk()
        getTrendingTvSeriesUseCase = mockk()
        getPopularTvSeriesUseCase = mockk()
        getTopRatedTvSeriesUseCase = mockk()

        viewModel = HomeViewModel(
            languageCode = languageCode,
            getMovieGenresUseCase = getMovieGenresUseCase,
            getTvSeriesGenresUseCase = getTvSeriesGenresUseCase,
            getTrendingMoviesUseCase = getTrendingMoviesUseCase,
            getNowPlayingMoviesUseCase = getNowPlayingMoviesUseCase,
            getPopularMoviesUseCase = getPopularMoviesUseCase,
            getTopRatedMoviesUseCase = getTopRatedMoviesUseCase,
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
            getAiringTodayTvSeriesUseCase = getAiringTodayTvSeriesUseCase,
            getOnAirTvSeriesUseCase = getOnAirTvSeriesUseCase,
            getTrendingTvSeriesUseCase = getTrendingTvSeriesUseCase,
            getPopularTvSeriesUseCase = getPopularTvSeriesUseCase,
            getTopRatedTvSeriesUseCase = getTopRatedTvSeriesUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMoviesForAllCategories should update state with all movie categories successfully`() = runTest(testDispatcher) {
        val movieGenres = listOf(Genre(id = 28, name = "Action"))
        val trendingMovies = listOf(createTestMovie(id = 1, title = "Trending Movie"))
        val nowPlayingMovies = listOf(createTestMovie(id = 2, title = "Now Playing Movie"))
        val popularMovies = listOf(createTestMovie(id = 3, title = "Popular Movie"))
        val topRatedMovies = listOf(createTestMovie(id = 4, title = "Top Rated Movie"))
        val upcomingMovies = listOf(createTestMovie(id = 5, title = "Upcoming Movie"))

        coEvery { getMovieGenresUseCase(languageCode) } returns movieGenres
        coEvery { getTrendingMoviesUseCase(1, languageCode) } returns trendingMovies
        coEvery { getNowPlayingMoviesUseCase(1, languageCode) } returns nowPlayingMovies
        coEvery { getPopularMoviesUseCase(1, languageCode) } returns popularMovies
        coEvery { getTopRatedMoviesUseCase(1, languageCode) } returns topRatedMovies
        coEvery { getUpcomingMoviesUseCase(1, languageCode) } returns upcomingMovies

        viewModel.moviesUiState.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            assertTrue(initialState.trendingMovies.isEmpty())

            viewModel.loadMoviesForAllCategories(1)
            advanceUntilIdle()

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertNull(successState.error)
            assertEquals(trendingMovies, successState.trendingMovies)
            assertEquals(nowPlayingMovies, successState.nowPlayingMovies)
            assertEquals(popularMovies, successState.popularMovies)
            assertEquals(topRatedMovies, successState.topRatedMovies)
            assertEquals(upcomingMovies, successState.upcomingMovies)
            assertEquals(1, successState.genreUiMovies.size)
            assertEquals("Action", successState.genreUiMovies[0].name)
        }

        coVerify { getMovieGenresUseCase(languageCode) }
        coVerify { getTrendingMoviesUseCase(1, languageCode) }
        coVerify { getNowPlayingMoviesUseCase(1, languageCode) }
        coVerify { getPopularMoviesUseCase(1, languageCode) }
        coVerify { getTopRatedMoviesUseCase(1, languageCode) }
        coVerify { getUpcomingMoviesUseCase(1, languageCode) }
    }

    @Test
    fun `loadMoviesForAllCategories should handle error and update state with error message`() = runTest(testDispatcher) {
        val errorMessage = "Network error"
        coEvery { getMovieGenresUseCase(languageCode) } throws Exception(errorMessage)

        viewModel.loadMoviesForAllCategories(1)
        advanceUntilIdle()

        val state = viewModel.moviesUiState.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        assertTrue(state.trendingMovies.isEmpty())
        assertTrue(state.nowPlayingMovies.isEmpty())
        assertTrue(state.popularMovies.isEmpty())
        assertTrue(state.topRatedMovies.isEmpty())
        assertTrue(state.upcomingMovies.isEmpty())

        coVerify { getMovieGenresUseCase(languageCode) }
    }

    @Test
    fun `loadMoviesForAllCategories should not load if trending movies already exist`() = runTest(testDispatcher) {
        val movieGenres = listOf(Genre(id = 28, name = "Action"))
        val trendingMovies = listOf(createTestMovie(id = 1, title = "Trending Movie"))
        val nowPlayingMovies = listOf(createTestMovie(id = 2, title = "Now Playing Movie"))
        val popularMovies = listOf(createTestMovie(id = 3, title = "Popular Movie"))
        val topRatedMovies = listOf(createTestMovie(id = 4, title = "Top Rated Movie"))
        val upcomingMovies = listOf(createTestMovie(id = 5, title = "Upcoming Movie"))

        coEvery { getMovieGenresUseCase(languageCode) } returns movieGenres
        coEvery { getTrendingMoviesUseCase(1, languageCode) } returns trendingMovies
        coEvery { getNowPlayingMoviesUseCase(1, languageCode) } returns nowPlayingMovies
        coEvery { getPopularMoviesUseCase(1, languageCode) } returns popularMovies
        coEvery { getTopRatedMoviesUseCase(1, languageCode) } returns topRatedMovies
        coEvery { getUpcomingMoviesUseCase(1, languageCode) } returns upcomingMovies

        viewModel.loadMoviesForAllCategories(1)
        advanceUntilIdle()

        viewModel.loadMoviesForAllCategories(1)
        advanceUntilIdle()

        coVerify(exactly = 1) { getMovieGenresUseCase(languageCode) }
        coVerify(exactly = 1) { getTrendingMoviesUseCase(1, languageCode) }
        coVerify(exactly = 1) { getNowPlayingMoviesUseCase(1, languageCode) }
        coVerify(exactly = 1) { getPopularMoviesUseCase(1, languageCode) }
        coVerify(exactly = 1) { getTopRatedMoviesUseCase(1, languageCode) }
        coVerify(exactly = 1) { getUpcomingMoviesUseCase(1, languageCode) }
    }

    @Test
    fun `loadTvSeriesForAllCategories should update state with all TV series categories successfully`() = runTest(testDispatcher) {
        val tvSeriesGenres = listOf(Genre(id = 18, name = "Drama"))
        val airingTodayTvSeries = listOf(createTestTvSeries(id = 1, name = "Airing Today"))
        val onTheAirTvSeries = listOf(createTestTvSeries(id = 2, name = "On The Air"))
        val trendingTvSeries = listOf(createTestTvSeries(id = 3, name = "Trending"))
        val popularTvSeries = listOf(createTestTvSeries(id = 4, name = "Popular"))
        val topRatedTvSeries = listOf(createTestTvSeries(id = 5, name = "Top Rated"))

        coEvery { getTvSeriesGenresUseCase(languageCode) } returns tvSeriesGenres
        coEvery { getAiringTodayTvSeriesUseCase(1, languageCode) } returns airingTodayTvSeries
        coEvery { getOnAirTvSeriesUseCase(1, languageCode) } returns onTheAirTvSeries
        coEvery { getTrendingTvSeriesUseCase(1, languageCode) } returns trendingTvSeries
        coEvery { getPopularTvSeriesUseCase(1, languageCode) } returns popularTvSeries
        coEvery { getTopRatedTvSeriesUseCase(1, languageCode) } returns topRatedTvSeries

        viewModel.tvSeriesUiState.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)

            viewModel.loadTvSeriesForAllCategories(1)
            advanceUntilIdle()

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertNull(successState.error)
            assertEquals(airingTodayTvSeries, successState.airingTodayTvSeries)
            assertEquals(onTheAirTvSeries, successState.onTheAirTvSeries)
            assertEquals(trendingTvSeries, successState.trendingTvSeries)
            assertEquals(popularTvSeries, successState.popularTvSeries)
            assertEquals(topRatedTvSeries, successState.topRatedTvSeries)
            assertEquals(1, successState.genreUiTvSeries.size)
            assertEquals("Drama", successState.genreUiTvSeries[0].name)
        }

        coVerify { getTvSeriesGenresUseCase(languageCode) }
        coVerify { getAiringTodayTvSeriesUseCase(1, languageCode) }
        coVerify { getOnAirTvSeriesUseCase(1, languageCode) }
        coVerify { getTrendingTvSeriesUseCase(1, languageCode) }
        coVerify { getPopularTvSeriesUseCase(1, languageCode) }
        coVerify { getTopRatedTvSeriesUseCase(1, languageCode) }
    }

    @Test
    fun `loadTvSeriesForAllCategories should handle error and update state with error message`() = runTest(testDispatcher) {
        val errorMessage = "Failed to load TV series"
        coEvery { getTvSeriesGenresUseCase(languageCode) } throws Exception(errorMessage)

        viewModel.loadTvSeriesForAllCategories(1)
        advanceUntilIdle()

        val state = viewModel.tvSeriesUiState.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        assertTrue(state.airingTodayTvSeries.isEmpty())
        assertTrue(state.onTheAirTvSeries.isEmpty())
        assertTrue(state.trendingTvSeries.isEmpty())
        assertTrue(state.popularTvSeries.isEmpty())
        assertTrue(state.topRatedTvSeries.isEmpty())

        coVerify { getTvSeriesGenresUseCase(languageCode) }
    }

    @Test
    fun `loadTvSeriesForAllCategories should not load if trending TV series already exist`() = runTest(testDispatcher) {
        val tvSeriesGenres = listOf(Genre(id = 18, name = "Drama"))
        val airingTodayTvSeries = listOf(createTestTvSeries(id = 1, name = "Airing Today"))
        val onTheAirTvSeries = listOf(createTestTvSeries(id = 2, name = "On The Air"))
        val trendingTvSeries = listOf(createTestTvSeries(id = 3, name = "Trending"))
        val popularTvSeries = listOf(createTestTvSeries(id = 4, name = "Popular"))
        val topRatedTvSeries = listOf(createTestTvSeries(id = 5, name = "Top Rated"))

        coEvery { getTvSeriesGenresUseCase(languageCode) } returns tvSeriesGenres
        coEvery { getAiringTodayTvSeriesUseCase(1, languageCode) } returns airingTodayTvSeries
        coEvery { getOnAirTvSeriesUseCase(1, languageCode) } returns onTheAirTvSeries
        coEvery { getTrendingTvSeriesUseCase(1, languageCode) } returns trendingTvSeries
        coEvery { getPopularTvSeriesUseCase(1, languageCode) } returns popularTvSeries
        coEvery { getTopRatedTvSeriesUseCase(1, languageCode) } returns topRatedTvSeries

        viewModel.loadTvSeriesForAllCategories(1)
        advanceUntilIdle()

        viewModel.loadTvSeriesForAllCategories(1)
        advanceUntilIdle()

        coVerify(exactly = 1) { getTvSeriesGenresUseCase(languageCode) }
        coVerify(exactly = 1) { getAiringTodayTvSeriesUseCase(1, languageCode) }
        coVerify(exactly = 1) { getOnAirTvSeriesUseCase(1, languageCode) }
        coVerify(exactly = 1) { getTrendingTvSeriesUseCase(1, languageCode) }
        coVerify(exactly = 1) { getPopularTvSeriesUseCase(1, languageCode) }
        coVerify(exactly = 1) { getTopRatedTvSeriesUseCase(1, languageCode) }
    }

    @Test
    fun `initial movies state should be loading with empty lists`() {
        val state = viewModel.moviesUiState.value

        assertTrue(state.isLoading)
        assertNull(state.error)
        assertTrue(state.trendingMovies.isEmpty())
        assertTrue(state.nowPlayingMovies.isEmpty())
        assertTrue(state.popularMovies.isEmpty())
        assertTrue(state.topRatedMovies.isEmpty())
        assertTrue(state.upcomingMovies.isEmpty())
        assertTrue(state.genreUiMovies.isEmpty())
    }

    @Test
    fun `initial TV series state should be loading with empty lists`() {
        val state = viewModel.tvSeriesUiState.value

        assertTrue(state.isLoading)
        assertNull(state.error)
        assertTrue(state.airingTodayTvSeries.isEmpty())
        assertTrue(state.onTheAirTvSeries.isEmpty())
        assertTrue(state.trendingTvSeries.isEmpty())
        assertTrue(state.popularTvSeries.isEmpty())
        assertTrue(state.topRatedTvSeries.isEmpty())
        assertTrue(state.genreUiTvSeries.isEmpty())
    }

    private fun createTestMovie(
        id: Int,
        title: String,
        posterPath: String = "/poster.jpg",
        backdropPath: String = "/backdrop.jpg",
        overview: String = "Test overview",
        releaseDate: String = "2024-01-01",
        voteAverage: Float = 8.5f,
        voteCount: Int = 1000,
        genreIds: List<Int> = listOf(1, 2),
        popularity: Float = 100.0f
    ) = Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        genreIds = genreIds,
        popularity = popularity,
        adult = false,
        video = false,
        originalTitle = title,
        originalLanguage = "en"
    )

    private fun createTestTvSeries(
        id: Int,
        name: String,
        posterPath: String = "/poster.jpg",
        backdropPath: String = "/backdrop.jpg",
        overview: String = "Test overview",
        firstAirDate: String = "2024-01-01",
        voteAverage: Float = 8.5f,
        voteCount: Int = 1000,
        genreIds: List<Int> = listOf(1, 2),
        popularity: Float = 100.0f
    ) = TvSeries(
        id = id,
        title = name,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        firstAirDate = firstAirDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        genreIds = genreIds,
        popularity = popularity,
        adult = false,
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalName = name
    )
}