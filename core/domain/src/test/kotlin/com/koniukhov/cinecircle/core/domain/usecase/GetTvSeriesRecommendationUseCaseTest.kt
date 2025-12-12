package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTvSeriesRecommendationUseCaseTest {

    private lateinit var repository: TvSeriesRepository
    private lateinit var useCase: GetTvSeriesRecommendationUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetTvSeriesRecommendationUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke returns tv series recommendations from repository`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"
        val page = 1
        val expectedRecommendations = listOf(
            TvSeries(
                id = 1668,
                title = "Friends",
                originalName = "Friends",
                overview = "Comedy series",
                posterPath = "/poster1.jpg",
                backdropPath = "/backdrop1.jpg",
                firstAirDate = "1994-09-22",
                voteAverage = 8.4f,
                voteCount = 5000,
                popularity = 150.0f,
                originCountry = listOf("US"),
                originalLanguage = "en",
                genreIds = listOf(35),
                adult = false
            ),
            TvSeries(
                id = 1399,
                title = "Game of Thrones",
                originalName = "Game of Thrones",
                overview = "Fantasy series",
                posterPath = "/poster2.jpg",
                backdropPath = "/backdrop2.jpg",
                firstAirDate = "2011-04-17",
                voteAverage = 8.4f,
                voteCount = 20000,
                popularity = 300.0f,
                originCountry = listOf("US"),
                originalLanguage = "en",
                genreIds = listOf(10765, 18),
                adult = false
            )
        )

        coEvery { repository.getTvSeriesRecommendations(tvSeriesId, page, languageCode) } returns expectedRecommendations

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(expectedRecommendations, result)
        coVerify(exactly = 1) { repository.getTvSeriesRecommendations(tvSeriesId, page, languageCode) }
    }

    @Test
    fun `invoke returns empty list when no recommendations available`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"
        val page = 1

        coEvery { repository.getTvSeriesRecommendations(tvSeriesId, page, languageCode) } returns emptyList()

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(emptyList<TvSeries>(), result)
        coVerify(exactly = 1) { repository.getTvSeriesRecommendations(tvSeriesId, page, languageCode) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"
        val page = 1

        coEvery { repository.getTvSeriesRecommendations(tvSeriesId, page, languageCode) } throws Exception("Network error")

        useCase(tvSeriesId, languageCode, page)
    }

    @Test
    fun `invoke handles different tv series ids correctly`() = runTest {
        val tvSeriesId = 1668
        val languageCode = "en"
        val page = 1
        val expectedRecommendations = listOf(
            TvSeries(
                id = 2710,
                title = "How I Met Your Mother",
                originalName = "How I Met Your Mother",
                overview = "Comedy series",
                posterPath = "/poster3.jpg",
                backdropPath = "/backdrop3.jpg",
                firstAirDate = "2005-09-19",
                voteAverage = 8.0f,
                voteCount = 4000,
                popularity = 120.0f,
                originCountry = listOf("US"),
                originalLanguage = "en",
                genreIds = listOf(35),
                adult = false
            )
        )

        coEvery { repository.getTvSeriesRecommendations(tvSeriesId, page, languageCode) } returns expectedRecommendations

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(expectedRecommendations, result)
        coVerify(exactly = 1) { repository.getTvSeriesRecommendations(tvSeriesId, page, languageCode) }
    }

    @Test
    fun `invoke handles pagination correctly`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"
        val page = 2
        val expectedRecommendations = listOf(
            TvSeries(
                id = 60059,
                title = "Better Call Saul",
                originalName = "Better Call Saul",
                overview = "Drama series",
                posterPath = "/poster4.jpg",
                backdropPath = "/backdrop4.jpg",
                firstAirDate = "2015-02-08",
                voteAverage = 8.8f,
                voteCount = 7000,
                popularity = 180.0f,
                originCountry = listOf("US"),
                originalLanguage = "en",
                genreIds = listOf(18, 80),
                adult = false
            )
        )

        coEvery { repository.getTvSeriesRecommendations(tvSeriesId, page, languageCode) } returns expectedRecommendations

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(expectedRecommendations, result)
        coVerify(exactly = 1) { repository.getTvSeriesRecommendations(tvSeriesId, page, languageCode) }
    }

    @Test
    fun `invoke handles multiple recommendations correctly`() = runTest {
        val tvSeriesId = 1399
        val languageCode = "en"
        val page = 1
        val expectedRecommendations = listOf(
            TvSeries(
                id = 1,
                title = "Series 1",
                originalName = "Series 1",
                overview = "Overview 1",
                posterPath = "/p1.jpg",
                backdropPath = "/b1.jpg",
                firstAirDate = "2020-01-01",
                voteAverage = 8.0f,
                voteCount = 100,
                popularity = 50.0f,
                originCountry = listOf("US"),
                originalLanguage = "en",
                genreIds = listOf(18),
                adult = false),
            TvSeries(
                id = 2,
                title = "Series 2",
                originalName = "Series 2",
                overview = "Overview 2",
                posterPath = "/p2.jpg",
                backdropPath = "/b2.jpg",
                firstAirDate = "2020-02-01",
                voteAverage = 7.5f,
                voteCount = 200,
                popularity = 60.0f,
                originCountry = listOf("US"),
                originalLanguage = "en",
                genreIds = listOf(35),
                adult = false),
            TvSeries(
                id = 3,
                title = "Series 3",
                originalName = "Series 3",
                overview = "Overview 3",
                posterPath = "/p3.jpg",
                backdropPath = "/b3.jpg",
                firstAirDate = "2020-03-01",
                voteAverage = 8.5f,
                voteCount = 150,
                popularity = 70.0f,
                originCountry = listOf("GB"),
                originalLanguage = "en",
                genreIds = listOf(80),
                adult = false)
        )

        coEvery { repository.getTvSeriesRecommendations(tvSeriesId, page, languageCode) } returns expectedRecommendations

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(expectedRecommendations, result)
        assertEquals(3, result.size)
        coVerify(exactly = 1) { repository.getTvSeriesRecommendations(tvSeriesId, page, languageCode) }
    }
}