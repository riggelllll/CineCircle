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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GetOnAirTvSeriesUseCaseTest {

    private lateinit var repository: TvSeriesRepository
    private lateinit var useCase: GetOnAirTvSeriesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetOnAirTvSeriesUseCase(repository)
    }

    @Test
    fun `invoke returns on air tv series from repository`() = runTest {
        val page = 1
        val language = "en"
        val expectedSeries = listOf(
            createTestTvSeries(id = 1, title = "On Air Series 1"),
            createTestTvSeries(id = 2, title = "On Air Series 2")
        )

        coEvery { repository.getOnTheAirTvSeries(page, language) } returns expectedSeries

        val result = useCase(page, language)

        assertEquals(expectedSeries, result)
        assertEquals(2, result.size)
        assertEquals("On Air Series 1", result[0].title)
        coVerify(exactly = 1) { repository.getOnTheAirTvSeries(page, language) }
    }

    @Test
    fun `invoke returns empty list when repository returns empty`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getOnTheAirTvSeries(page, language) } returns emptyList()

        val result = useCase(page, language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getOnTheAirTvSeries(page, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getOnTheAirTvSeries(page, language) } throws Exception("Network error")

        useCase(page, language)
    }

    @Test
    fun `invoke with different page numbers calls repository correctly`() = runTest {
        val page1 = 1
        val page2 = 2
        val language = "en"
        val series = listOf<TvSeries>()

        coEvery { repository.getOnTheAirTvSeries(any(), any()) } returns series

        useCase(page1, language)
        useCase(page2, language)

        coVerify(exactly = 1) { repository.getOnTheAirTvSeries(page1, language) }
        coVerify(exactly = 1) { repository.getOnTheAirTvSeries(page2, language) }
    }

    @Test
    fun `invoke returns series currently airing`() = runTest {
        val page = 1
        val language = "en"
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ISO_DATE

        val series = listOf(
            createTestTvSeries(id = 1, firstAirDate = today.minusDays(30).format(formatter)),
            createTestTvSeries(id = 2, firstAirDate = today.minusDays(60).format(formatter)),
            createTestTvSeries(id = 3, firstAirDate = today.minusDays(90).format(formatter))
        )

        coEvery { repository.getOnTheAirTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        result.forEach { tvSeries ->
            assertTrue("Series should have air date", tvSeries.firstAirDate.isNotEmpty())
        }
    }

    @Test
    fun `invoke returns series with good ratings`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, voteAverage = 8.5f, voteCount = 1000),
            createTestTvSeries(id = 2, voteAverage = 8.0f, voteCount = 800),
            createTestTvSeries(id = 3, voteAverage = 7.8f, voteCount = 600)
        )

        coEvery { repository.getOnTheAirTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        assertTrue(result.all { it.voteAverage >= 7.0f })
        assertTrue(result.all { it.voteCount > 0 })
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val page = 1
        val languageEn = "en"
        val languageEs = "es"
        val series = listOf<TvSeries>()

        coEvery { repository.getOnTheAirTvSeries(any(), any()) } returns series

        useCase(page, languageEn)
        useCase(page, languageEs)

        coVerify(exactly = 1) { repository.getOnTheAirTvSeries(page, languageEn) }
        coVerify(exactly = 1) { repository.getOnTheAirTvSeries(page, languageEs) }
    }

    @Test
    fun `invoke returns series from various genres`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, genreIds = listOf(18)),
            createTestTvSeries(id = 2, genreIds = listOf(35)),
            createTestTvSeries(id = 3, genreIds = listOf(10765))
        )

        coEvery { repository.getOnTheAirTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        val allGenres = result.flatMap { it.genreIds }.toSet()
        assertTrue(allGenres.contains(18))
        assertTrue(allGenres.contains(35))
        assertTrue(allGenres.contains(10765))
    }

    @Test
    fun `invoke returns series with popularity scores`() = runTest {
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, popularity = 500.0f),
            createTestTvSeries(id = 2, popularity = 450.0f),
            createTestTvSeries(id = 3, popularity = 400.0f)
        )

        coEvery { repository.getOnTheAirTvSeries(page, language) } returns series

        val result = useCase(page, language)

        assertEquals(3, result.size)
        assertTrue(result.all { it.popularity > 0.0f })
    }

    private fun createTestTvSeries(
        id: Int = 1,
        title: String = "Test Series",
        voteAverage: Float = 8.0f,
        voteCount: Int = 500,
        popularity: Float = 450.0f,
        firstAirDate: String = "2024-01-01",
        genreIds: List<Int> = listOf(18, 10765)
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
        genreIds = genreIds,
        originCountry = listOf("US")
    )
}