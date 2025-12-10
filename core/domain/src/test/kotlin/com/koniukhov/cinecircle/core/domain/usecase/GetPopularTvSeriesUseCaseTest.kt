package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPopularTvSeriesUseCaseTest {

    private lateinit var repository: TvSeriesRepository
    private lateinit var useCase: GetPopularTvSeriesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetPopularTvSeriesUseCase(repository)
    }

    @Test
    fun `invoke returns popular tv series from repository`() = runTest {
        val page = 1
        val language = "en"
        val expectedSeries = listOf(
            createTestTvSeries(id = 1, title = "Popular Series 1", popularity = 500.0f),
            createTestTvSeries(id = 2, title = "Popular Series 2", popularity = 450.0f)
        )

        coEvery { repository.getPopularTvSeries(page, language) } returns expectedSeries

        val result = useCase(page, language)

        assertEquals(expectedSeries, result)
        assertEquals(2, result.size)
        assertEquals("Popular Series 1", result[0].title)
        coVerify(exactly = 1) { repository.getPopularTvSeries(page, language) }
    }

    @Test
    fun `invoke returns empty list when repository returns empty`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getPopularTvSeries(page, language) } returns emptyList()

        val result = useCase(page, language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getPopularTvSeries(page, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getPopularTvSeries(page, language) } throws Exception("Network error")

        useCase(page, language)
    }

    @Test
    fun `invoke with different page numbers calls repository correctly`() = runTest {
        val page1 = 1
        val page2 = 2
        val language = "en"
        val series = listOf<TvSeries>()

        coEvery { repository.getPopularTvSeries(any(), any()) } returns series

        useCase(page1, language)
        useCase(page2, language)

        coVerify(exactly = 1) { repository.getPopularTvSeries(page1, language) }
        coVerify(exactly = 1) { repository.getPopularTvSeries(page2, language) }
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val page = 1
        val languageEn = "en"
        val languageEs = "es"
        val series = listOf<TvSeries>()

        coEvery { repository.getPopularTvSeries(any(), any()) } returns series

        useCase(page, languageEn)
        useCase(page, languageEs)

        coVerify(exactly = 1) { repository.getPopularTvSeries(page, languageEn) }
        coVerify(exactly = 1) { repository.getPopularTvSeries(page, languageEs) }
    }

    @Test
    fun `invoke returns series sorted by popularity`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, title = "Most Popular", popularity = 1000.0f),
            createTestTvSeries(id = 2, title = "Second Popular", popularity = 800.0f),
            createTestTvSeries(id = 3, title = "Third Popular", popularity = 600.0f)
        )

        coEvery { repository.getPopularTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        assertTrue(result[0].popularity >= result[1].popularity)
        assertTrue(result[1].popularity >= result[2].popularity)
    }

    @Test
    fun `invoke returns series with high vote average`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, voteAverage = 8.5f, voteCount = 1000),
            createTestTvSeries(id = 2, voteAverage = 8.0f, voteCount = 800),
            createTestTvSeries(id = 3, voteAverage = 7.8f, voteCount = 600)
        )

        coEvery { repository.getPopularTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        assertTrue(result.all { it.voteAverage >= 7.0f })
        assertTrue(result.all { it.voteCount > 0 })
    }

    @Test
    fun `invoke returns series from different origin countries`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, originCountry = listOf("US")),
            createTestTvSeries(id = 2, originCountry = listOf("GB")),
            createTestTvSeries(id = 3, originCountry = listOf("KR"))
        )

        coEvery { repository.getPopularTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        val countries = result.flatMap { it.originCountry }.toSet()
        assertTrue(countries.contains("US"))
        assertTrue(countries.contains("GB"))
        assertTrue(countries.contains("KR"))
    }

    private fun createTestTvSeries(
        id: Int = 1,
        title: String = "Test Series",
        popularity: Float = 100.0f,
        voteAverage: Float = 7.5f,
        voteCount: Int = 100,
        originCountry: List<String> = listOf("US")
    ) = TvSeries(
        id = id,
        title = title,
        overview = "Test overview",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        firstAirDate = "2024-01-01",
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        adult = false,
        originalLanguage = "en",
        originalName = title,
        genreIds = listOf(18, 10765),
        originCountry = originCountry
    )
}