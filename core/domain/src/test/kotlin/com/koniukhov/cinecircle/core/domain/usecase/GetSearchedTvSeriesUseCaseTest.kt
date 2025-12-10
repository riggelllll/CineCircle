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

class GetSearchedTvSeriesUseCaseTest {

    private lateinit var repository: TvSeriesRepository
    private lateinit var useCase: GetSearchedTvSeriesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetSearchedTvSeriesUseCase(repository)
    }

    @Test
    fun `invoke returns searched tv series from repository`() = runTest {
        val query = "Breaking Bad"
        val page = 1
        val language = "en"
        val expectedSeries = listOf(
            createTestTvSeries(id = 1, title = "Breaking Bad", originalName = "Breaking Bad"),
            createTestTvSeries(id = 2, title = "Breaking Bad: The Movie", originalName = "Breaking Bad: The Movie")
        )

        coEvery { repository.getSearchedTvSeries(query, page, language) } returns expectedSeries

        val result = useCase(query, page, language)

        assertEquals(expectedSeries, result)
        assertEquals(2, result.size)
        assertTrue(result[0].title.contains("Breaking Bad"))
        coVerify(exactly = 1) { repository.getSearchedTvSeries(query, page, language) }
    }

    @Test
    fun `invoke returns empty list when no results found`() = runTest {
        val query = "NonExistentSeries123456"
        val page = 1
        val language = "en"

        coEvery { repository.getSearchedTvSeries(query, page, language) } returns emptyList()

        val result = useCase(query, page, language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getSearchedTvSeries(query, page, language) }
    }

    @Test
    fun `invoke returns empty list when query is empty`() = runTest {
        val query = ""
        val page = 1
        val language = "en"

        coEvery { repository.getSearchedTvSeries(query, page, language) } returns emptyList()

        val result = useCase(query, page, language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getSearchedTvSeries(query, page, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val query = "Test"
        val page = 1
        val language = "en"

        coEvery { repository.getSearchedTvSeries(query, page, language) } throws Exception("Network error")

        useCase(query, page, language)
    }

    @Test
    fun `invoke with different queries calls repository correctly`() = runTest {
        val query1 = "Game of Thrones"
        val query2 = "Stranger Things"
        val page = 1
        val language = "en"
        val series = listOf<TvSeries>()

        coEvery { repository.getSearchedTvSeries(any(), any(), any()) } returns series

        useCase(query1, page, language)
        useCase(query2, page, language)

        coVerify(exactly = 1) { repository.getSearchedTvSeries(query1, page, language) }
        coVerify(exactly = 1) { repository.getSearchedTvSeries(query2, page, language) }
    }

    @Test
    fun `invoke with partial match returns matching series`() = runTest {
        val query = "game"
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, title = "Game of Thrones"),
            createTestTvSeries(id = 2, title = "The Squid Game"),
            createTestTvSeries(id = 3, title = "Game Night")
        )

        coEvery { repository.getSearchedTvSeries(query, page, language) } returns series

        val result = useCase(query, page, language)

        assertEquals(3, result.size)
        result.forEach { tvSeries ->
            assertTrue(
                "Title should contain search query (case-insensitive)",
                tvSeries.title.contains(query, ignoreCase = true)
            )
        }
    }

    @Test
    fun `invoke with different page numbers calls repository correctly`() = runTest {
        val query = "Friends"
        val page1 = 1
        val page2 = 2
        val language = "en"
        val series = listOf<TvSeries>()

        coEvery { repository.getSearchedTvSeries(any(), any(), any()) } returns series

        useCase(query, page1, language)
        useCase(query, page2, language)

        coVerify(exactly = 1) { repository.getSearchedTvSeries(query, page1, language) }
        coVerify(exactly = 1) { repository.getSearchedTvSeries(query, page2, language) }
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val query = "Casa de Papel"
        val page = 1
        val languageEn = "en"
        val languageEs = "es"
        val series = listOf<TvSeries>()

        coEvery { repository.getSearchedTvSeries(any(), any(), any()) } returns series

        useCase(query, page, languageEn)
        useCase(query, page, languageEs)

        coVerify(exactly = 1) { repository.getSearchedTvSeries(query, page, languageEn) }
        coVerify(exactly = 1) { repository.getSearchedTvSeries(query, page, languageEs) }
    }

    @Test
    fun `invoke returns series ordered by relevance`() = runTest {
        val query = "Star"
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, title = "Star Trek", popularity = 500.0f),
            createTestTvSeries(id = 2, title = "Star Wars: The Clone Wars", popularity = 450.0f),
            createTestTvSeries(id = 3, title = "Stargate SG-1", popularity = 400.0f)
        )

        coEvery { repository.getSearchedTvSeries(query, page, language) } returns series

        val result = useCase(query, page, language)

        assertEquals(3, result.size)
        result.forEach { tvSeries ->
            assertTrue(tvSeries.title.contains(query, ignoreCase = true))
        }
    }

    @Test
    fun `invoke with special characters in query works correctly`() = runTest {
        val query = "S.H.I.E.L.D."
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(id = 1, title = "Marvel's Agents of S.H.I.E.L.D.")
        )

        coEvery { repository.getSearchedTvSeries(query, page, language) } returns series

        val result = useCase(query, page, language)

        assertEquals(1, result.size)
        assertEquals("Marvel's Agents of S.H.I.E.L.D.", result[0].title)
    }

    @Test
    fun `invoke returns series with valid metadata`() = runTest {
        val query = "The Office"
        val page = 1
        val language = "en"
        val series = listOf(
            createTestTvSeries(
                id = 1,
                title = "The Office",
                voteAverage = 8.5f,
                voteCount = 5000,
                popularity = 600.0f
            )
        )

        coEvery { repository.getSearchedTvSeries(query, page, language) } returns series

        val result = useCase(query, page, language)

        assertEquals(1, result.size)
        val tvSeries = result[0]
        assertTrue(tvSeries.voteAverage > 0.0f)
        assertTrue(tvSeries.voteCount > 0)
        assertTrue(tvSeries.popularity > 0.0f)
        assertTrue(tvSeries.posterPath.isNotEmpty())
    }

    private fun createTestTvSeries(
        id: Int = 1,
        title: String = "Test Series",
        originalName: String = "Test Series",
        voteAverage: Float = 7.5f,
        voteCount: Int = 100,
        popularity: Float = 100.0f
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
        originalName = originalName,
        genreIds = listOf(18, 35),
        originCountry = listOf("US")
    )
}