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

class GetFilteredTvSeriesUseCaseTest {

    private lateinit var repository: TvSeriesRepository
    private lateinit var useCase: GetFilteredTvSeriesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetFilteredTvSeriesUseCase(repository)
    }

    @Test
    fun `invoke returns filtered tv series with all parameters from repository`() = runTest {
        val page = 1
        val language = "en"
        val sortBy = "popularity.desc"
        val airDateGte = "2023-01-01"
        val airDateLte = "2023-12-31"
        val year = 2023
        val firstAirDateGte = "2023-01-01"
        val firstAirDateLte = "2023-12-31"
        val minVoteAverage = 7.0f
        val maxVoteAverage = 10.0f
        val minVoteCount = 100
        val maxVoteCount = 10000
        val withOriginCountry = "US"
        val withOriginalLanguage = "en"
        val withGenres = "18,10765"
        val withoutGenres = "27"

        val mockTvSeries = listOf(
            TvSeries(
                id = 1,
                title = "Test Series 1",
                overview = "Overview 1",
                posterPath = "/poster1.jpg",
                backdropPath = "/backdrop1.jpg",
                voteAverage = 8.5f,
                voteCount = 1000,
                firstAirDate = "2023-06-15",
                originCountry = listOf("US"),
                originalLanguage = "en",
                originalName = "Test Series 1",
                popularity = 100.0f,
                adult = false,
                genreIds = listOf(18, 10765)
            )
        )

        coEvery {
            repository.getFilteredTvSeries(
                page = page,
                language = language,
                sortBy = sortBy,
                airDateGte = airDateGte,
                airDateLte = airDateLte,
                year = year,
                firstAirDateGte = firstAirDateGte,
                firstAirDateLte = firstAirDateLte,
                minVoteAverage = minVoteAverage,
                maxVoteAverage = maxVoteAverage,
                minVoteCount = minVoteCount,
                maxVoteCount = maxVoteCount,
                withOriginCountry = withOriginCountry,
                withOriginalLanguage = withOriginalLanguage,
                withGenres = withGenres,
                withoutGenres = withoutGenres
            )
        } returns mockTvSeries

        val result = useCase(
            page = page,
            language = language,
            sortBy = sortBy,
            airDateGte = airDateGte,
            airDateLte = airDateLte,
            year = year,
            firstAirDateGte = firstAirDateGte,
            firstAirDateLte = firstAirDateLte,
            minVoteAverage = minVoteAverage,
            maxVoteAverage = maxVoteAverage,
            minVoteCount = minVoteCount,
            maxVoteCount = maxVoteCount,
            withOriginCountry = withOriginCountry,
            withOriginalLanguage = withOriginalLanguage,
            withGenres = withGenres,
            withoutGenres = withoutGenres
        )

        assertEquals(mockTvSeries, result)
        coVerify(exactly = 1) {
            repository.getFilteredTvSeries(
                page = page,
                language = language,
                sortBy = sortBy,
                airDateGte = airDateGte,
                airDateLte = airDateLte,
                year = year,
                firstAirDateGte = firstAirDateGte,
                firstAirDateLte = firstAirDateLte,
                minVoteAverage = minVoteAverage,
                maxVoteAverage = maxVoteAverage,
                minVoteCount = minVoteCount,
                maxVoteCount = maxVoteCount,
                withOriginCountry = withOriginCountry,
                withOriginalLanguage = withOriginalLanguage,
                withGenres = withGenres,
                withoutGenres = withoutGenres
            )
        }
    }

    @Test
    fun `invoke returns filtered tv series with minimal parameters from repository`() = runTest {
        val page = 1
        val language = "en"
        val sortBy = "popularity.desc"

        val mockTvSeries = emptyList<TvSeries>()

        coEvery {
            repository.getFilteredTvSeries(
                page = page,
                language = language,
                sortBy = sortBy,
                airDateGte = null,
                airDateLte = null,
                year = null,
                firstAirDateGte = null,
                firstAirDateLte = null,
                minVoteAverage = null,
                maxVoteAverage = null,
                minVoteCount = null,
                maxVoteCount = null,
                withOriginCountry = null,
                withOriginalLanguage = null,
                withGenres = null,
                withoutGenres = null
            )
        } returns mockTvSeries

        val result = useCase(
            page = page,
            language = language,
            sortBy = sortBy
        )

        assertEquals(mockTvSeries, result)
        coVerify(exactly = 1) {
            repository.getFilteredTvSeries(
                page = page,
                language = language,
                sortBy = sortBy,
                airDateGte = null,
                airDateLte = null,
                year = null,
                firstAirDateGte = null,
                firstAirDateLte = null,
                minVoteAverage = null,
                maxVoteAverage = null,
                minVoteCount = null,
                maxVoteCount = null,
                withOriginCountry = null,
                withOriginalLanguage = null,
                withGenres = null,
                withoutGenres = null
            )
        }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val page = 1
        val language = "en"
        val sortBy = "popularity.desc"

        coEvery {
            repository.getFilteredTvSeries(
                page = page,
                language = language,
                sortBy = sortBy,
                airDateGte = null,
                airDateLte = null,
                year = null,
                firstAirDateGte = null,
                firstAirDateLte = null,
                minVoteAverage = null,
                maxVoteAverage = null,
                minVoteCount = null,
                maxVoteCount = null,
                withOriginCountry = null,
                withOriginalLanguage = null,
                withGenres = null,
                withoutGenres = null
            )
        } throws Exception("Network error")

        useCase(
            page = page,
            language = language,
            sortBy = sortBy
        )
    }
}