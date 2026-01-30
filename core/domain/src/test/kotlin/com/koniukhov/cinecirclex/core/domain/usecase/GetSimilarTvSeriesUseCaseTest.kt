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

class GetSimilarTvSeriesUseCaseTest {

    private lateinit var repository: TvSeriesRepository
    private lateinit var useCase: GetSimilarTvSeriesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetSimilarTvSeriesUseCase(repository)
    }

    @Test
    fun `invoke returns similar tv series from repository`() = runTest {
        val tvSeriesId = 1
        val languageCode = "en"
        val page = 1
        val expectedSeries = listOf(
            createTestTvSeries(id = 2, title = "Similar Series 1", genreIds = listOf(18, 10765)),
            createTestTvSeries(id = 3, title = "Similar Series 2", genreIds = listOf(18, 10765))
        )

        coEvery { repository.getSimilarTvSeries(tvSeriesId, page, languageCode) } returns expectedSeries

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(expectedSeries, result)
        assertEquals(2, result.size)
        assertEquals("Similar Series 1", result[0].title)
        coVerify(exactly = 1) { repository.getSimilarTvSeries(tvSeriesId, page, languageCode) }
    }

    @Test
    fun `invoke returns empty list when no similar series found`() = runTest {
        val tvSeriesId = 1
        val languageCode = "en"
        val page = 1

        coEvery { repository.getSimilarTvSeries(tvSeriesId, page, languageCode) } returns emptyList()

        val result = useCase(tvSeriesId, languageCode, page)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getSimilarTvSeries(tvSeriesId, page, languageCode) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val tvSeriesId = 1
        val languageCode = "en"
        val page = 1

        coEvery { repository.getSimilarTvSeries(tvSeriesId, page, languageCode) } throws Exception("Network error")

        useCase(tvSeriesId, languageCode, page)
    }

    @Test
    fun `invoke with different tv series IDs calls repository correctly`() = runTest {
        val tvSeriesId1 = 1
        val tvSeriesId2 = 2
        val languageCode = "en"
        val page = 1
        val series = listOf<TvSeries>()

        coEvery { repository.getSimilarTvSeries(any(), any(), any()) } returns series

        useCase(tvSeriesId1, languageCode, page)
        useCase(tvSeriesId2, languageCode, page)

        coVerify(exactly = 1) { repository.getSimilarTvSeries(tvSeriesId1, page, languageCode) }
        coVerify(exactly = 1) { repository.getSimilarTvSeries(tvSeriesId2, page, languageCode) }
    }

    @Test
    fun `invoke with invalid tv series ID returns empty list`() = runTest {
        val tvSeriesId = -1
        val languageCode = "en"
        val page = 1

        coEvery { repository.getSimilarTvSeries(tvSeriesId, page, languageCode) } returns emptyList()

        val result = useCase(tvSeriesId, languageCode, page)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke returns series with matching genres`() = runTest {
        val tvSeriesId = 1
        val languageCode = "en"
        val page = 1
        val targetGenres = listOf(18, 10765)

        val series = listOf(
            createTestTvSeries(id = 2, genreIds = listOf(18, 10765)),
            createTestTvSeries(id = 3, genreIds = listOf(18, 35)),
            createTestTvSeries(id = 4, genreIds = listOf(10765, 80))
        )

        coEvery { repository.getSimilarTvSeries(tvSeriesId, page, languageCode) } returns series

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(3, result.size)
        result.forEach { tvSeries ->
            assertTrue(
                "Series should have at least one matching genre",
                tvSeries.genreIds.any { it in targetGenres }
            )
        }
    }

    @Test
    fun `invoke with different page numbers calls repository correctly`() = runTest {
        val tvSeriesId = 1
        val languageCode = "en"
        val page1 = 1
        val page2 = 2
        val series = listOf<TvSeries>()

        coEvery { repository.getSimilarTvSeries(any(), any(), any()) } returns series

        useCase(tvSeriesId, languageCode, page1)
        useCase(tvSeriesId, languageCode, page2)

        coVerify(exactly = 1) { repository.getSimilarTvSeries(tvSeriesId, page1, languageCode) }
        coVerify(exactly = 1) { repository.getSimilarTvSeries(tvSeriesId, page2, languageCode) }
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val tvSeriesId = 1
        val languageEn = "en"
        val languageEs = "es"
        val page = 1
        val series = listOf<TvSeries>()

        coEvery { repository.getSimilarTvSeries(any(), any(), any()) } returns series

        useCase(tvSeriesId, languageEn, page)
        useCase(tvSeriesId, languageEs, page)

        coVerify(exactly = 1) { repository.getSimilarTvSeries(tvSeriesId, page, languageEn) }
        coVerify(exactly = 1) { repository.getSimilarTvSeries(tvSeriesId, page, languageEs) }
    }

    @Test
    fun `invoke returns series sorted by vote average`() = runTest {
        val tvSeriesId = 1
        val languageCode = "en"
        val page = 1
        val series = listOf(
            createTestTvSeries(id = 2, title = "Highest Rated", voteAverage = 9.0f),
            createTestTvSeries(id = 3, title = "Second Rated", voteAverage = 8.5f),
            createTestTvSeries(id = 4, title = "Third Rated", voteAverage = 8.0f)
        )

        coEvery { repository.getSimilarTvSeries(tvSeriesId, page, languageCode) } returns series

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(3, result.size)
        assertTrue(result[0].voteAverage >= result[1].voteAverage)
        assertTrue(result[1].voteAverage >= result[2].voteAverage)
    }

    @Test
    fun `invoke returns series with good ratings`() = runTest {
        val tvSeriesId = 1
        val languageCode = "en"
        val page = 1
        val series = listOf(
            createTestTvSeries(id = 2, voteAverage = 8.5f, voteCount = 1000),
            createTestTvSeries(id = 3, voteAverage = 8.0f, voteCount = 800),
            createTestTvSeries(id = 4, voteAverage = 7.8f, voteCount = 600)
        )

        coEvery { repository.getSimilarTvSeries(tvSeriesId, page, languageCode) } returns series

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(3, result.size)
        assertTrue(result.all { it.voteAverage >= 7.0f })
        assertTrue(result.all { it.voteCount > 0 })
    }

    @Test
    fun `invoke returns series from same origin countries`() = runTest {
        val tvSeriesId = 1
        val languageCode = "en"
        val page = 1
        val series = listOf(
            createTestTvSeries(id = 2, originCountry = listOf("US")),
            createTestTvSeries(id = 3, originCountry = listOf("US")),
            createTestTvSeries(id = 4, originCountry = listOf("GB"))
        )

        coEvery { repository.getSimilarTvSeries(tvSeriesId, page, languageCode) } returns series

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(3, result.size)
        val countries = result.flatMap { it.originCountry }.toSet()
        assertTrue(countries.isNotEmpty())
    }

    private fun createTestTvSeries(
        id: Int = 1,
        title: String = "Test Series",
        voteAverage: Float = 8.0f,
        voteCount: Int = 500,
        genreIds: List<Int> = listOf(18, 10765),
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
        popularity = 100.0f,
        adult = false,
        originalLanguage = "en",
        originalName = title,
        genreIds = genreIds,
        originCountry = originCountry
    )
}