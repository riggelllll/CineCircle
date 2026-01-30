package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.TvSeries
import com.koniukhov.cinecirclex.core.domain.repository.TvSeriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetTrendingTvSeriesUseCaseTest {

    private lateinit var repository: TvSeriesRepository
    private lateinit var useCase: GetTrendingTvSeriesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetTrendingTvSeriesUseCase(repository)
    }

    @Test
    fun `invoke returns trending tv series from repository`() = runTest {
        val page = 1
        val language = "en"
        val expectedSeries = listOf(
            createTestTvSeries(id = 1, title = "Trending Series 1", popularity = 1500.0f),
            createTestTvSeries(id = 2, title = "Trending Series 2", popularity = 1200.0f)
        )

        coEvery { repository.getTrendingTvSeries(page, language) } returns expectedSeries

        val result = useCase(page, language)

        assertEquals(expectedSeries, result)
        assertEquals(2, result.size)
        assertEquals("Trending Series 1", result[0].title)
        coVerify(exactly = 1) { repository.getTrendingTvSeries(page, language) }
    }

    @Test
    fun `invoke returns empty list when repository returns empty`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getTrendingTvSeries(page, language) } returns emptyList()

        val result = useCase(page, language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getTrendingTvSeries(page, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getTrendingTvSeries(page, language) } throws Exception("Network error")

        useCase(page, language)
    }

    @Test
    fun `invoke with different page numbers calls repository correctly`() = runTest {
        val page1 = 1
        val page2 = 2
        val language = "en"
        val series = listOf<TvSeries>()

        coEvery { repository.getTrendingTvSeries(any(), any()) } returns series

        useCase(page1, language)
        useCase(page2, language)

        coVerify(exactly = 1) { repository.getTrendingTvSeries(page1, language) }
        coVerify(exactly = 1) { repository.getTrendingTvSeries(page2, language) }
    }

    @Test
    fun `invoke returns series sorted by popularity descending`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, title = "Most Trending", popularity = 2000.0f),
            createTestTvSeries(id = 2, title = "Second Trending", popularity = 1500.0f),
            createTestTvSeries(id = 3, title = "Third Trending", popularity = 1000.0f)
        )

        coEvery { repository.getTrendingTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        assertTrue(result[0].popularity >= result[1].popularity)
        assertTrue(result[1].popularity >= result[2].popularity)
        assertEquals(2000.0f, result[0].popularity)
    }

    @Test
    fun `invoke returns highly popular series`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, popularity = 2500.0f, voteAverage = 8.5f),
            createTestTvSeries(id = 2, popularity = 2000.0f, voteAverage = 8.2f),
            createTestTvSeries(id = 3, popularity = 1800.0f, voteAverage = 8.0f)
        )

        coEvery { repository.getTrendingTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        assertTrue(result.all { it.popularity >= 1000.0f })
        assertTrue(result.all { it.voteAverage >= 7.5f })
    }

    @Test
    fun `invoke returns series from various countries`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, originCountry = listOf("US")),
            createTestTvSeries(id = 2, originCountry = listOf("GB")),
            createTestTvSeries(id = 3, originCountry = listOf("KR")),
            createTestTvSeries(id = 4, originCountry = listOf("JP"))
        )

        coEvery { repository.getTrendingTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(4, result.size)
        val countries = result.flatMap { it.originCountry }.toSet()
        assertTrue(countries.contains("US"))
        assertTrue(countries.contains("GB"))
        assertTrue(countries.contains("KR"))
        assertTrue(countries.contains("JP"))
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val page = 1
        val languageEn = "en"
        val languageEs = "es"
        val languageKo = "ko"
        val series = listOf<TvSeries>()

        coEvery { repository.getTrendingTvSeries(any(), any()) } returns series

        useCase(page, languageEn)
        useCase(page, languageEs)
        useCase(page, languageKo)

        coVerify(exactly = 1) { repository.getTrendingTvSeries(page, languageEn) }
        coVerify(exactly = 1) { repository.getTrendingTvSeries(page, languageEs) }
        coVerify(exactly = 1) { repository.getTrendingTvSeries(page, languageKo) }
    }

    @Test
    fun `invoke returns series with recent air dates`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, firstAirDate = "2024-12-01"),
            createTestTvSeries(id = 2, firstAirDate = "2024-11-15"),
            createTestTvSeries(id = 3, firstAirDate = "2024-10-20")
        )

        coEvery { repository.getTrendingTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        result.forEach { series ->
            assertTrue("Series should have air date", series.firstAirDate.isNotEmpty())
        }
    }

    private fun createTestTvSeries(
        id: Int = 1,
        title: String = "Test Series",
        popularity: Float = 1000.0f,
        voteAverage: Float = 8.0f,
        voteCount: Int = 500,
        originCountry: List<String> = listOf("US"),
        firstAirDate: String = "2024-01-01"
    ) = TvSeries(
        id = id,
        title = title,
        overview = "Test overview",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        firstAirDate = firstAirDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        adult = false,
        originalLanguage = "en",
        originalName = title,
        genreIds = listOf(18, 23),
        originCountry = originCountry
    )
}