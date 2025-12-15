package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.local.LocalMoviesDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteMoviesDataSource
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.network.model.MovieDto
import com.koniukhov.cinecircle.core.network.model.MovieDetailsDto
import com.koniukhov.cinecircle.core.network.model.MoviesResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MoviesRepositoryImplTest {

    private lateinit var repository: MoviesRepositoryImpl
    private lateinit var remoteDataSource: RemoteMoviesDataSource
    private lateinit var localDataSource: LocalMoviesDataSource
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @Before
    fun setup() {
        remoteDataSource = mockk()
        localDataSource = mockk()
        networkStatusProvider = mockk()

        repository = MoviesRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            networkStatusProvider = networkStatusProvider
        )
    }

    @Test
    fun `getTrendingMovies should return movies when network is available`() = runTest {
        val movieDto = createTestMovieDto(id = 1, title = "Test Movie")
        val response = MoviesResponseDto(results = listOf(movieDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTrendingMovies(any(), any()) } returns response

        val result = repository.getTrendingMovies(page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Test Movie", result[0].title)
        coVerify { remoteDataSource.getTrendingMovies(1, "en") }
    }

    @Test
    fun `getTrendingMovies should return empty list when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTrendingMovies(any(), any()) } throws Exception("Network error")

        val result = repository.getTrendingMovies(page = 1, language = "en")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getNowPlayingMovies should return movies when network is available`() = runTest {
        val movieDto = createTestMovieDto(id = 2, title = "Now Playing Movie")
        val response = MoviesResponseDto(results = listOf(movieDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getNowPlayingMovies(any(), any()) } returns response

        val result = repository.getNowPlayingMovies(page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Now Playing Movie", result[0].title)
    }

    @Test
    fun `getPopularMovies should return movies when network is available`() = runTest {
        val movieDto = createTestMovieDto(id = 3, title = "Popular Movie")
        val response = MoviesResponseDto(results = listOf(movieDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getPopularMovies(any(), any()) } returns response

        val result = repository.getPopularMovies(page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Popular Movie", result[0].title)
    }

    @Test
    fun `getTopRatedMovies should return movies when network is available`() = runTest {
        val movieDto = createTestMovieDto(id = 4, title = "Top Rated Movie")
        val response = MoviesResponseDto(results = listOf(movieDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTopRatedMovies(any(), any()) } returns response

        val result = repository.getTopRatedMovies(page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Top Rated Movie", result[0].title)
    }

    @Test
    fun `getUpcomingMovies should return movies when network is available`() = runTest {
        val movieDto = createTestMovieDto(id = 5, title = "Upcoming Movie")
        val response = MoviesResponseDto(results = listOf(movieDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getUpcomingMovies(any(), any()) } returns response

        val result = repository.getUpcomingMovies(page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Upcoming Movie", result[0].title)
    }

    @Test
    fun `getMoviesByGenre should return movies filtered by genre`() = runTest {
        val movieDto = createTestMovieDto(id = 6, title = "Action Movie")
        val response = MoviesResponseDto(results = listOf(movieDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMoviesByGenre(any(), any(), any()) } returns response

        val result = repository.getMoviesByGenre(genreId = 28, page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Action Movie", result[0].title)
        coVerify { remoteDataSource.getMoviesByGenre(28, 1, "en") }
    }

    @Test
    fun `getMovieDetails should return movie details from remote when available`() = runTest {
        val movieDetailsDto = createTestMovieDetailsDto(id = 7, title = "Detailed Movie")

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieDetails(any(), any()) } returns movieDetailsDto
        coEvery { localDataSource.getMovieWithGenres(any()) } returns null

        val result = repository.getMovieDetails(movieId = 7, language = "en")

        assertEquals(7, result.id)
        assertEquals("Detailed Movie", result.title)
        coVerify { remoteDataSource.getMovieDetails(7, "en") }
    }

    @Test
    fun `getMovieRecommendations should return recommended movies`() = runTest {
        val movieDto = createTestMovieDto(id = 8, title = "Recommended Movie")
        val response = MoviesResponseDto(results = listOf(movieDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieRecommendations(any(), any(), any()) } returns response

        val result = repository.getMovieRecommendations(movieId = 100, page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Recommended Movie", result[0].title)
    }

    @Test
    fun `getSimilarMovies should return similar movies`() = runTest {
        val movieDto = createTestMovieDto(id = 9, title = "Similar Movie")
        val response = MoviesResponseDto(results = listOf(movieDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getSimilarMovies(any(), any(), any()) } returns response

        val result = repository.getSimilarMovies(movieId = 100, page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Similar Movie", result[0].title)
    }

    @Test
    fun `getFilteredMovies should return filtered movies with all parameters`() = runTest {
        val movieDto = createTestMovieDto(id = 11, title = "Filtered Movie")
        val response = MoviesResponseDto(results = listOf(movieDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getFilteredMovies(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns response

        val result = repository.getFilteredMovies(
            page = 1,
            language = "en",
            sortBy = "popularity.desc",
            year = 2023,
            releaseDateGte = "2023-01-01",
            releaseDateLte = "2023-12-31",
            minVoteAverage = 7.0f,
            maxVoteAverage = 10.0f,
            minVoteCount = 100,
            maxVoteCount = 10000,
            withOriginCountry = "US",
            withOriginalLanguage = "en",
            withGenres = "28",
            withoutGenres = "27"
        )

        assertEquals(1, result.size)
        assertEquals("Filtered Movie", result[0].title)
    }

    @Test
    fun `getFilteredMovies should handle empty results`() = runTest {
        val response = MoviesResponseDto(results = emptyList(), page = 1, totalPages = 0, totalResults = 0)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getFilteredMovies(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns response

        val result = repository.getFilteredMovies(
            page = 1,
            language = "en",
            sortBy = "popularity.desc"
        )

        assertTrue(result.isEmpty())
    }

    private fun createTestMovieDto(
        id: Int,
        title: String,
        posterPath: String? = "/test.jpg",
        backdropPath: String? = "/backdrop.jpg",
        overview: String = "Test overview",
        releaseDate: String = "2023-01-01",
        voteAverage: Float = 7.5f,
        voteCount: Int = 1000
    ) = MovieDto(
        id = id,
        title = title,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        genreIds = listOf(28, 12),
        originalLanguage = "en",
        originalTitle = title,
        popularity = 100.0f,
        adult = false,
        video = false
    )

    private fun createTestMovieDetailsDto(
        id: Int,
        title: String
    ) = MovieDetailsDto(
        id = id,
        title = title,
        posterPath = "/test.jpg",
        backdropPath = "/backdrop.jpg",
        overview = "Test overview",
        releaseDate = "2023-01-01",
        voteAverage = 7.5f,
        voteCount = 1000,
        genres = emptyList(),
        originalLanguage = "en",
        originalTitle = title,
        popularity = 100.0f,
        adult = false,
        video = false,
        budget = 1000000,
        belongsToCollection = null,
        homePage = "http://test.com",
        imdbId = "tt1234567",
        revenue = 2000000,
        runtime = 120,
        productionCompanies = emptyList(),
        productionCountries = emptyList(),
        spokenLanguages = emptyList(),
        status = "Released",
        tagline = "Test tagline"
    )
}