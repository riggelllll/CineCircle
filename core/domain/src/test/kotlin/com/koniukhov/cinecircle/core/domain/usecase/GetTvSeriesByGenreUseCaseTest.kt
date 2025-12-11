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

class GetTvSeriesByGenreUseCaseTest {

    private lateinit var repository: TvSeriesRepository
    private lateinit var useCase: GetTvSeriesByGenreUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetTvSeriesByGenreUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke returns list of tv series from repository`() = runTest {
        val genreId = 18
        val page = 1
        val language = "en"
        val expectedTvSeries = listOf(
            TvSeries(
                id = 1,
                title = "Breaking Bad",
                originalName = "Breaking Bad",
                overview = "Drama series",
                posterPath = "/poster1.jpg",
                backdropPath = "/backdrop1.jpg",
                firstAirDate = "2008-01-20",
                voteAverage = 9.5f,
                voteCount = 1000,
                popularity = 100.0f,
                originCountry = listOf("US"),
                originalLanguage = "en",
                genreIds = listOf(18),
                adult = false
            ),
            TvSeries(
                id = 2,
                title = "The Wire",
                originalName = "The Wire",
                overview = "Crime drama",
                posterPath = "/poster2.jpg",
                backdropPath = "/backdrop2.jpg",
                firstAirDate = "2002-06-02",
                voteAverage = 9.3f,
                voteCount = 800,
                popularity = 90.0f,
                originCountry = listOf("US"),
                originalLanguage = "en",
                genreIds = listOf(18, 80),
                adult = false
            )
        )

        coEvery { repository.getTvSeriesByGenre(genreId, page, language) } returns expectedTvSeries

        val result = useCase(genreId, page, language)

        assertEquals(expectedTvSeries, result)
        coVerify(exactly = 1) { repository.getTvSeriesByGenre(genreId, page, language) }
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runTest {
        val genreId = 18
        val page = 1
        val language = "en"

        coEvery { repository.getTvSeriesByGenre(genreId, page, language) } returns emptyList()

        val result = useCase(genreId, page, language)

        assertEquals(emptyList<TvSeries>(), result)
        coVerify(exactly = 1) { repository.getTvSeriesByGenre(genreId, page, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val genreId = 18
        val page = 1
        val language = "en"

        coEvery { repository.getTvSeriesByGenre(genreId, page, language) } throws Exception("Network error")

        useCase(genreId, page, language)
    }

    @Test
    fun `invoke handles different genre ids correctly`() = runTest {
        val genreId = 10765
        val page = 1
        val language = "en"
        val expectedTvSeries = listOf(
            TvSeries(
                id = 3,
                title = "Doctor Who",
                originalName = "Doctor Who",
                overview = "Sci-Fi series",
                posterPath = "/poster3.jpg",
                backdropPath = "/backdrop3.jpg",
                firstAirDate = "2005-03-26",
                voteAverage = 8.2f,
                voteCount = 500,
                popularity = 80.0f,
                originCountry = listOf("GB"),
                originalLanguage = "en",
                genreIds = listOf(10765),
                adult = false
            )
        )

        coEvery { repository.getTvSeriesByGenre(genreId, page, language) } returns expectedTvSeries

        val result = useCase(genreId, page, language)

        assertEquals(expectedTvSeries, result)
        coVerify(exactly = 1) { repository.getTvSeriesByGenre(genreId, page, language) }
    }

    @Test
    fun `invoke handles pagination correctly`() = runTest {
        val genreId = 18
        val page = 2
        val language = "en"
        val expectedTvSeries = listOf(
            TvSeries(
                id = 4,
                title = "Better Call Saul",
                originalName = "Better Call Saul",
                overview = "Drama series",
                posterPath = "/poster4.jpg",
                backdropPath = "/backdrop4.jpg",
                firstAirDate = "2015-02-08",
                voteAverage = 8.8f,
                voteCount = 700,
                popularity = 85.0f,
                originCountry = listOf("US"),
                originalLanguage = "en",
                genreIds = listOf(18),
                adult = false
            )
        )

        coEvery { repository.getTvSeriesByGenre(genreId, page, language) } returns expectedTvSeries

        val result = useCase(genreId, page, language)

        assertEquals(expectedTvSeries, result)
        coVerify(exactly = 1) { repository.getTvSeriesByGenre(genreId, page, language) }
    }
}