package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetAiringTodayTvSeriesUseCaseTest {

    private lateinit var repository: TvSeriesRepository
    private lateinit var useCase: GetAiringTodayTvSeriesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetAiringTodayTvSeriesUseCase(repository)
    }

    @Test
    fun `invoke returns airing today tv series from repository`() = runTest {
        val page = 1
        val language = "en"
        val mockTvSeries = listOf(
            TvSeries(
                id = 1,
                title = "Test Series 1",
                overview = "Overview 1",
                posterPath = "/poster1.jpg",
                backdropPath = "/backdrop1.jpg",
                voteAverage = 8.5f,
                voteCount = 100,
                firstAirDate = "2023-01-01",
                originCountry = listOf("US"),
                originalLanguage = "en",
                originalName = "Test Series 1",
                popularity = 100.0f,
                adult = false,
                genreIds = listOf(28, 14)
            ),
            TvSeries(
                id = 2,
                title = "Test Series 2",
                overview = "Overview 2",
                posterPath = "/poster2.jpg",
                backdropPath = "/backdrop2.jpg",
                voteAverage = 7.5f,
                voteCount = 50,
                firstAirDate = "2023-02-01",
                originCountry = listOf("US"),
                originalLanguage = "en",
                originalName = "Test Series 2",
                popularity = 80.0f,
                adult = false,
                genreIds = listOf(35, 18)
            )
        )

        coEvery { repository.getAiringTodayTvSeries(page, language) } returns mockTvSeries

        val result = useCase(page, language)

        assertEquals(mockTvSeries, result)
        coVerify(exactly = 1) { repository.getAiringTodayTvSeries(page, language) }
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getAiringTodayTvSeries(page, language) } returns emptyList()

        val result = useCase(page, language)

        assertEquals(emptyList<TvSeries>(), result)
        coVerify(exactly = 1) { repository.getAiringTodayTvSeries(page, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getAiringTodayTvSeries(page, language) } throws Exception("Network error")

        useCase(page, language)
    }
}