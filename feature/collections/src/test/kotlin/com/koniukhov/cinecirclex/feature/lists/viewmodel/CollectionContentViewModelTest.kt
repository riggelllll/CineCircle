package com.koniukhov.cinecirclex.feature.lists.viewmodel

import app.cash.turbine.test
import com.koniukhov.cinecirclex.core.database.repository.MediaListRepository
import com.koniukhov.cinecirclex.core.domain.model.Genre
import com.koniukhov.cinecirclex.core.domain.model.Movie
import com.koniukhov.cinecirclex.core.domain.model.MovieCollection
import com.koniukhov.cinecirclex.core.domain.model.MovieDetails
import com.koniukhov.cinecirclex.core.domain.model.TvEpisodeDetails
import com.koniukhov.cinecirclex.core.domain.model.TvSeries
import com.koniukhov.cinecirclex.core.domain.model.TvSeriesDetails
import com.koniukhov.cinecirclex.core.domain.usecase.GetMovieDetailsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesDetailsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionContentViewModelTest {

    private lateinit var viewModel: CollectionContentViewModel
    private lateinit var mediaListRepository: MediaListRepository
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var getTvSeriesDetailsUseCase: GetTvSeriesDetailsUseCase

    private val testDispatcher = StandardTestDispatcher()
    private val languageCode = "en"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mediaListRepository = mockk(relaxed = true)
        getMovieDetailsUseCase = mockk()
        getTvSeriesDetailsUseCase = mockk()

        viewModel = CollectionContentViewModel(
            mediaListRepository = mediaListRepository,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            getTvSeriesDetailsUseCase = getTvSeriesDetailsUseCase,
            languageCode = languageCode
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCollectionContent should return movies and tv series`() = runTest(testDispatcher) {
        val collectionId = 1L
        val movieIds = listOf(123, 456)
        val tvIds = listOf(789)

        val movieDetails1 = createTestMovieDetails(
            id = 123,
            title = "Test Movie 1"
        )
        val movieDetails2 = createTestMovieDetails(
            id = 456,
            title = "Test Movie 2"
        )
        val tvSeriesDetails = createTestTvSeriesDetails(
            id = 789,
            name = "Test TV Series"
        )

        coEvery { mediaListRepository.getMoviesInList(collectionId) } returns flowOf(movieIds)
        coEvery { mediaListRepository.getTvSeriesInList(collectionId) } returns flowOf(tvIds)
        coEvery { getMovieDetailsUseCase(123, languageCode) } returns movieDetails1
        coEvery { getMovieDetailsUseCase(456, languageCode) } returns movieDetails2
        coEvery { getTvSeriesDetailsUseCase(789, languageCode) } returns tvSeriesDetails

        viewModel.getCollectionContent(collectionId).test {
            val items = awaitItem()
            assertEquals(3, items.size)

            val movies = items.filterIsInstance<Movie>()
            assertEquals(2, movies.size)
            assertEquals("Test Movie 1", movies[0].title)
            assertEquals("Test Movie 2", movies[1].title)

            val tvSeries = items.filterIsInstance<TvSeries>()
            assertEquals(1, tvSeries.size)
            assertEquals("Test TV Series", tvSeries[0].title)

            awaitComplete()
        }

        coVerify { mediaListRepository.getMoviesInList(collectionId) }
        coVerify { mediaListRepository.getTvSeriesInList(collectionId) }
        coVerify { getMovieDetailsUseCase(123, languageCode) }
        coVerify { getMovieDetailsUseCase(456, languageCode) }
        coVerify { getTvSeriesDetailsUseCase(789, languageCode) }
    }

    @Test
    fun `getCollectionContent should return empty list when no items`() = runTest(testDispatcher) {
        val collectionId = 1L

        coEvery { mediaListRepository.getMoviesInList(collectionId) } returns flowOf(emptyList())
        coEvery { mediaListRepository.getTvSeriesInList(collectionId) } returns flowOf(emptyList())

        viewModel.getCollectionContent(collectionId).test {
            val items = awaitItem()
            assertTrue(items.isEmpty())
            awaitComplete()
        }

        coVerify { mediaListRepository.getMoviesInList(collectionId) }
        coVerify { mediaListRepository.getTvSeriesInList(collectionId) }
    }

    @Test
    fun `getCollectionContent should handle movie details fetch failure`() = runTest(testDispatcher) {
        val collectionId = 1L
        val movieIds = listOf(123, 456)

        coEvery { mediaListRepository.getMoviesInList(collectionId) } returns flowOf(movieIds)
        coEvery { mediaListRepository.getTvSeriesInList(collectionId) } returns flowOf(emptyList())
        coEvery { getMovieDetailsUseCase(123, languageCode) } throws Exception("Network error")
        coEvery { getMovieDetailsUseCase(456, languageCode) } returns createTestMovieDetails(
            id = 456,
            title = "Test Movie 2"
        )

        viewModel.getCollectionContent(collectionId).test {
            val items = awaitItem()
            assertEquals(1, items.size)
            val movie = items[0] as Movie
            assertEquals("Test Movie 2", movie.title)
            awaitComplete()
        }

        coVerify { getMovieDetailsUseCase(123, languageCode) }
        coVerify { getMovieDetailsUseCase(456, languageCode) }
    }

    @Test
    fun `getCollectionContent should handle tv series details fetch failure`() = runTest(testDispatcher) {
        val collectionId = 1L
        val tvIds = listOf(789, 999)

        coEvery { mediaListRepository.getMoviesInList(collectionId) } returns flowOf(emptyList())
        coEvery { mediaListRepository.getTvSeriesInList(collectionId) } returns flowOf(tvIds)
        coEvery { getTvSeriesDetailsUseCase(789, languageCode) } throws Exception("Network error")
        coEvery { getTvSeriesDetailsUseCase(999, languageCode) } returns createTestTvSeriesDetails(
            id = 999,
            name = "Test TV Series 2"
        )

        viewModel.getCollectionContent(collectionId).test {
            val items = awaitItem()
            assertEquals(1, items.size)
            val tvSeries = items[0] as TvSeries
            assertEquals("Test TV Series 2", tvSeries.title)
            awaitComplete()
        }

        coVerify { getTvSeriesDetailsUseCase(789, languageCode) }
        coVerify { getTvSeriesDetailsUseCase(999, languageCode) }
    }

    @Test
    fun `getCollectionContent should return only movies when no tv series`() = runTest(testDispatcher) {
        val collectionId = 1L
        val movieIds = listOf(123)

        val movieDetails = createTestMovieDetails(
            id = 123,
            title = "Test Movie"
        )

        coEvery { mediaListRepository.getMoviesInList(collectionId) } returns flowOf(movieIds)
        coEvery { mediaListRepository.getTvSeriesInList(collectionId) } returns flowOf(emptyList())
        coEvery { getMovieDetailsUseCase(123, languageCode) } returns movieDetails

        viewModel.getCollectionContent(collectionId).test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertTrue(items[0] is Movie)
            awaitComplete()
        }
    }

    @Test
    fun `getCollectionContent should return only tv series when no movies`() = runTest(testDispatcher) {
        val collectionId = 1L
        val tvIds = listOf(789)

        val tvSeriesDetails = createTestTvSeriesDetails(
            id = 789,
            name = "Test TV Series"
        )

        coEvery { mediaListRepository.getMoviesInList(collectionId) } returns flowOf(emptyList())
        coEvery { mediaListRepository.getTvSeriesInList(collectionId) } returns flowOf(tvIds)
        coEvery { getTvSeriesDetailsUseCase(789, languageCode) } returns tvSeriesDetails

        viewModel.getCollectionContent(collectionId).test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertTrue(items[0] is TvSeries)
            awaitComplete()
        }
    }

    private fun createTestMovieDetails(
        id: Int,
        title: String,
        posterPath: String = "/poster.jpg",
        backdropPath: String = "/backdrop.jpg",
        overview: String = "Test overview",
        releaseDate: String = "2024-01-01",
        voteAverage: Float = 8.5f,
        voteCount: Int = 1000,
        genres: List<Genre> = listOf(Genre(28, "Action")),
        popularity: Float = 100.0f
    ) = MovieDetails(
        id = id,
        title = title,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        genres = genres,
        popularity = popularity,
        adult = false,
        video = false,
        originalTitle = title,
        originalLanguage = "en",
        budget = 1000000,
        revenue = 5000000,
        runtime = 120,
        status = "Released",
        tagline = "Test tagline",
        homePage = "https://test.com",
        imdbId = "tt1234567",
        productionCompanies = emptyList(),
        productionCountries = emptyList(),
        spokenLanguages = emptyList(),
        belongsToCollection = MovieCollection.empty()
    )

    private fun createTestTvSeriesDetails(
        id: Int,
        name: String,
        posterPath: String = "/poster.jpg",
        backdropPath: String = "/backdrop.jpg",
        overview: String = "Test overview",
        firstAirDate: String = "2024-01-01",
        voteAverage: Float = 8.5f,
        voteCount: Int = 1000,
        genres: List<Genre> = listOf(Genre(18, "Drama")),
        popularity: Float = 100.0f
    ) = TvSeriesDetails(
        id = id,
        name = name,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        firstAirDate = firstAirDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        genres = genres,
        popularity = popularity,
        adult = false,
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalName = name,
        numberOfSeasons = 3,
        numberOfEpisodes = 30,
        status = "Returning Series",
        type = "Scripted",
        homepage = "https://test.com",
        inProduction = true,
        languages = listOf("en"),
        lastAirDate = "2024-12-01",
        networks = emptyList(),
        productionCompanies = emptyList(),
        productionCountries = emptyList(),
        seasons = emptyList(),
        spokenLanguages = emptyList(),
        tagline = "Test tagline",
        createdBy = emptyList(),
        episodeRunTime = emptyList(),
        lastEpisodeToAir = TvEpisodeDetails.empty(),
        nextEpisodeToAir = null
    )
}