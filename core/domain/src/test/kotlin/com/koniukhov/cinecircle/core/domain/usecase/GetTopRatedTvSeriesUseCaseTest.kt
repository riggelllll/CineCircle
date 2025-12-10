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

class GetTopRatedTvSeriesUseCaseTest {

    private lateinit var repository: TvSeriesRepository
    private lateinit var useCase: GetTopRatedTvSeriesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetTopRatedTvSeriesUseCase(repository)
    }

    @Test
    fun `invoke returns top rated tv series from repository`() = runTest {
        val page = 1
        val language = "en"
        val expectedSeries = listOf(
            createTestTvSeries(id = 1, title = "Top Rated Series 1", voteAverage = 9.5f),
            createTestTvSeries(id = 2, title = "Top Rated Series 2", voteAverage = 9.0f)
        )

        coEvery { repository.getTopRatedTvSeries(page, language) } returns expectedSeries

        val result = useCase(page, language)

        assertEquals(expectedSeries, result)
        assertEquals(2, result.size)
        assertEquals("Top Rated Series 1", result[0].title)
        coVerify(exactly = 1) { repository.getTopRatedTvSeries(page, language) }
    }

    @Test
    fun `invoke returns empty list when repository returns empty`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getTopRatedTvSeries(page, language) } returns emptyList()

        val result = useCase(page, language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getTopRatedTvSeries(page, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getTopRatedTvSeries(page, language) } throws Exception("Network error")

        useCase(page, language)
    }

    @Test
    fun `invoke with different page numbers calls repository correctly`() = runTest {
        val page1 = 1
        val page2 = 2
        val language = "en"
        val series = listOf<TvSeries>()

        coEvery { repository.getTopRatedTvSeries(any(), any()) } returns series

        useCase(page1, language)
        useCase(page2, language)

        coVerify(exactly = 1) { repository.getTopRatedTvSeries(page1, language) }
        coVerify(exactly = 1) { repository.getTopRatedTvSeries(page2, language) }
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val page = 1
        val languageEn = "en"
        val languageEs = "es"
        val series = listOf<TvSeries>()

        coEvery { repository.getTopRatedTvSeries(any(), any()) } returns series

        useCase(page, languageEn)
        useCase(page, languageEs)

        coVerify(exactly = 1) { repository.getTopRatedTvSeries(page, languageEn) }
        coVerify(exactly = 1) { repository.getTopRatedTvSeries(page, languageEs) }
    }

    @Test
    fun `invoke returns series sorted by vote average`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, title = "Highest Rated", voteAverage = 9.5f, voteCount = 5000),
            createTestTvSeries(id = 2, title = "Second Highest", voteAverage = 9.0f, voteCount = 4000),
            createTestTvSeries(id = 3, title = "Third Highest", voteAverage = 8.8f, voteCount = 3000)
        )

        coEvery { repository.getTopRatedTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        assertTrue(result[0].voteAverage >= result[1].voteAverage)
        assertTrue(result[1].voteAverage >= result[2].voteAverage)
    }

    @Test
    fun `invoke returns series with high vote counts`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, voteAverage = 9.0f, voteCount = 10000),
            createTestTvSeries(id = 2, voteAverage = 8.8f, voteCount = 8000),
            createTestTvSeries(id = 3, voteAverage = 8.5f, voteCount = 6000)
        )

        coEvery { repository.getTopRatedTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        assertTrue(result.all { it.voteCount >= 1000 })
        assertTrue(result.all { it.voteAverage >= 8.0f })
    }

    @Test
    fun `invoke returns only highly rated series`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, voteAverage = 9.5f),
            createTestTvSeries(id = 2, voteAverage = 9.3f),
            createTestTvSeries(id = 3, voteAverage = 9.0f),
            createTestTvSeries(id = 4, voteAverage = 8.8f)
        )

        coEvery { repository.getTopRatedTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(4, result.size)
        assertTrue(result.all { it.voteAverage >= 8.5f })
        result.forEach { series ->
            assertTrue("Series ${series.title} should have rating >= 8.5", series.voteAverage >= 8.5f)
        }
    }

    @Test
    fun `invoke returns series with various genres`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, genreIds = listOf(18)),
            createTestTvSeries(id = 2, genreIds = listOf(80)),
            createTestTvSeries(id = 3, genreIds = listOf(10765))
        )

        coEvery { repository.getTopRatedTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        val allGenres = result.flatMap { it.genreIds }.toSet()
        assertTrue(allGenres.contains(18))
        assertTrue(allGenres.contains(80))
        assertTrue(allGenres.contains(10765))
    }

    private fun createTestTvSeries(
        id: Int = 1,
        title: String = "Test Series",
        voteAverage: Float = 8.5f,
        voteCount: Int = 1000,
        genreIds: List<Int> = listOf(18, 10765)
    ) = TvSeries(
        id = id,
        title = title,
        overview = "Test overview",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        firstAirDate = "2024-01-01",
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = 100.0f,
        adult = false,
        originalLanguage = "en",
        originalName = title,
        genreIds = genreIds,
        originCountry = listOf("US")
    )
}