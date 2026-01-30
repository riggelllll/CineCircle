package com.koniukhov.cinecirclex.core.data.repository

import com.koniukhov.cinecirclex.core.data.local.LocalTvSeriesDataSource
import com.koniukhov.cinecirclex.core.data.remote.RemoteTvSeriesDataSource
import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.core.network.model.TvSeriesDto
import com.koniukhov.cinecirclex.core.network.model.TvSeriesDetailsDto
import com.koniukhov.cinecirclex.core.network.model.TvSeriesResponseDto
import com.koniukhov.cinecirclex.core.network.model.TvSeasonDetailsDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TvSeriesRepositoryImplTest {

    private lateinit var repository: TvSeriesRepositoryImpl
    private lateinit var remoteDataSource: RemoteTvSeriesDataSource
    private lateinit var localDataSource: LocalTvSeriesDataSource
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @Before
    fun setup() {
        remoteDataSource = mockk()
        localDataSource = mockk()
        networkStatusProvider = mockk()

        repository = TvSeriesRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            networkStatusProvider = networkStatusProvider
        )
    }

    @Test
    fun `getAiringTodayTvSeries should return tv series when network is available`() = runTest {
        val tvSeriesDto = createTestTvSeriesDto(id = 1, name = "Test TV Series")
        val response = TvSeriesResponseDto(results = listOf(tvSeriesDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getAiringTodayTvSeries(any(), any()) } returns response

        val result = repository.getAiringTodayTvSeries(page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Test TV Series", result[0].title)
        coVerify { remoteDataSource.getAiringTodayTvSeries(1, "en") }
    }

    @Test
    fun `getAiringTodayTvSeries should return empty list when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getAiringTodayTvSeries(any(), any()) } throws Exception("Network error")

        val result = repository.getAiringTodayTvSeries(page = 1, language = "en")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getOnTheAirTvSeries should return tv series when network is available`() = runTest {
        val tvSeriesDto = createTestTvSeriesDto(id = 2, name = "On The Air Series")
        val response = TvSeriesResponseDto(results = listOf(tvSeriesDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getOnTheAirTvSeries(any(), any()) } returns response

        val result = repository.getOnTheAirTvSeries(page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("On The Air Series", result[0].title)
    }

    @Test
    fun `getTrendingTvSeries should return trending tv series`() = runTest {
        val tvSeriesDto = createTestTvSeriesDto(id = 3, name = "Trending Series")
        val response = TvSeriesResponseDto(results = listOf(tvSeriesDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTrendingTvSeries(any(), any()) } returns response

        val result = repository.getTrendingTvSeries(page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Trending Series", result[0].title)
    }

    @Test
    fun `getPopularTvSeries should return popular tv series`() = runTest {
        val tvSeriesDto = createTestTvSeriesDto(id = 4, name = "Popular Series")
        val response = TvSeriesResponseDto(results = listOf(tvSeriesDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getPopularTvSeries(any(), any()) } returns response

        val result = repository.getPopularTvSeries(page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Popular Series", result[0].title)
    }

    @Test
    fun `getTopRatedTvSeries should return top rated tv series`() = runTest {
        val tvSeriesDto = createTestTvSeriesDto(id = 5, name = "Top Rated Series")
        val response = TvSeriesResponseDto(results = listOf(tvSeriesDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTopRatedTvSeries(any(), any()) } returns response

        val result = repository.getTopRatedTvSeries(page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Top Rated Series", result[0].title)
    }

    @Test
    fun `getTvSeriesByGenre should return tv series filtered by genre`() = runTest {
        val tvSeriesDto = createTestTvSeriesDto(id = 6, name = "Drama Series")
        val response = TvSeriesResponseDto(results = listOf(tvSeriesDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesByGenre(any(), any(), any()) } returns response

        val result = repository.getTvSeriesByGenre(genreId = 18, page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Drama Series", result[0].title)
        coVerify { remoteDataSource.getTvSeriesByGenre(18, 1, "en") }
    }

    @Test
    fun `getTvSeriesDetails should return tv series details from remote when available`() = runTest {
        val tvSeriesDetailsDto = createTestTvSeriesDetailsDto(id = 7, name = "Detailed Series")

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesDetails(any(), any()) } returns tvSeriesDetailsDto
        coEvery { localDataSource.getTvSeriesWithGenres(any()) } returns null

        val result = repository.getTvSeriesDetails(id = 7, language = "en")

        assertEquals(7, result.id)
        assertEquals("Detailed Series", result.name)
        coVerify { remoteDataSource.getTvSeriesDetails(7, "en") }
    }

    @Test
    fun `getTvSeasonDetails should return season details`() = runTest {
        val seasonDetailsDto = createTestTvSeasonDetailsDto(seasonNumber = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeasonDetails(any(), any(), any()) } returns seasonDetailsDto

        val result = repository.getTvSeasonDetails(tvSeriesId = 100, seasonNumber = 1, language = "en")

        assertEquals(1, result.seasonNumber)
        coVerify { remoteDataSource.getTvSeasonDetails(100, 1, "en") }
    }

    @Test
    fun `getTvSeriesRecommendations should return recommended tv series`() = runTest {
        val tvSeriesDto = createTestTvSeriesDto(id = 8, name = "Recommended Series")
        val response = TvSeriesResponseDto(results = listOf(tvSeriesDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesRecommendations(any(), any(), any()) } returns response

        val result = repository.getTvSeriesRecommendations(tvSeriesId = 100, page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Recommended Series", result[0].title)
    }

    @Test
    fun `getSimilarTvSeries should return similar tv series`() = runTest {
        val tvSeriesDto = createTestTvSeriesDto(id = 9, name = "Similar Series")
        val response = TvSeriesResponseDto(results = listOf(tvSeriesDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getSimilarTvSeries(any(), any(), any()) } returns response

        val result = repository.getSimilarTvSeries(tvSeriesId = 100, page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("Similar Series", result[0].title)
    }

    @Test
    fun `getFilteredTvSeries should return filtered tv series with all parameters`() = runTest {
        val tvSeriesDto = createTestTvSeriesDto(id = 11, name = "Filtered Series")
        val response = TvSeriesResponseDto(results = listOf(tvSeriesDto), page = 1, totalPages = 1, totalResults = 1)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getFilteredTvSeries(
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any())
        } returns response

        val result = repository.getFilteredTvSeries(
            page = 1,
            language = "en",
            sortBy = "popularity.desc",
            airDateGte = "2023-01-01",
            airDateLte = "2023-12-31",
            year = 2023,
            firstAirDateGte = "2023-01-01",
            firstAirDateLte = "2023-12-31",
            minVoteAverage = 7.0f,
            maxVoteAverage = 10.0f,
            minVoteCount = 100,
            maxVoteCount = 10000,
            withOriginCountry = "US",
            withOriginalLanguage = "en",
            withGenres = "18",
            withoutGenres = "27"
        )

        assertEquals(1, result.size)
        assertEquals("Filtered Series", result[0].title)
    }

    @Test
    fun `getFilteredTvSeries should handle empty results`() = runTest {
        val response = TvSeriesResponseDto(results = emptyList(), page = 1, totalPages = 0, totalResults = 0)

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getFilteredTvSeries(
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any())
        } returns response

        val result = repository.getFilteredTvSeries(
            page = 1,
            language = "en",
            sortBy = "popularity.desc"
        )

        assertTrue(result.isEmpty())
    }

    private fun createTestTvSeriesDto(
        id: Int,
        name: String,
        posterPath: String? = "/test.jpg",
        backdropPath: String? = "/backdrop.jpg",
        overview: String = "Test overview",
        firstAirDate: String = "2023-01-01",
        voteAverage: Float = 7.5f,
        voteCount: Int = 1000
    ) = TvSeriesDto(
        id = id,
        name = name,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        firstAirDate = firstAirDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        genreIds = listOf(18, 10765),
        originalLanguage = "en",
        originCountry = listOf("US"),
        originalName = name,
        popularity = 100.0f,
        adult = false
    )

    private fun createTestTvSeriesDetailsDto(
        id: Int,
        name: String
    ) = TvSeriesDetailsDto(
        id = id,
        name = name,
        posterPath = "/test.jpg",
        backdropPath = "/backdrop.jpg",
        overview = "Test overview",
        firstAirDate = "2023-01-01",
        lastAirDate = "2023-12-31",
        voteAverage = 7.5f,
        voteCount = 1000,
        genres = emptyList(),
        originalLanguage = "en",
        originCountry = listOf("US"),
        originalName = name,
        popularity = 100.0f,
        adult = false,
        createdBy = emptyList(),
        episodeRunTime = emptyList(),
        homepage = "http://test.com",
        inProduction = true,
        languages = listOf("en"),
        lastEpisodeToAir = null,
        nextEpisodeToAir = null,
        networks = emptyList(),
        numberOfEpisodes = 10,
        numberOfSeasons = 1,
        productionCompanies = emptyList(),
        productionCountries = emptyList(),
        seasons = emptyList(),
        spokenLanguages = emptyList(),
        status = "Returning Series",
        tagline = "Test tagline",
        type = "Scripted"
    )

    private fun createTestTvSeasonDetailsDto(
        seasonNumber: Int
    ) = TvSeasonDetailsDto(
        _id = "season_${seasonNumber}_id",
        id = 1,
        airDate = "2023-01-01",
        episodes = emptyList(),
        name = "Season $seasonNumber",
        overview = "Test overview",
        posterPath = "/test.jpg",
        seasonNumber = seasonNumber,
        voteAverage = 7.5f
    )
}