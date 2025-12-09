package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetTrendingMoviesUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetTrendingMoviesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetTrendingMoviesUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke calls repository with correct parameters and returns result`() = runTest {
        val page = 1
        val language = "en"
        val expectedMovies = listOf(
            createTestMovie(id = 1, title = "Trending Movie 1"),
            createTestMovie(id = 2, title = "Trending Movie 2")
        )
        coEvery { repository.getTrendingMovies(page, language) } returns expectedMovies

        val result = useCase(page, language)

        assertEquals(expectedMovies, result)
        assertEquals(2, result.size)
        assertEquals("Trending Movie 1", result[0].title)
        coVerify(exactly = 1) { repository.getTrendingMovies(page, language) }
    }

    @Test
    fun `invoke with different page returns corresponding movies`() = runTest {
        val page = 2
        val language = "en"
        val expectedMovies = listOf(
            createTestMovie(id = 3, title = "Page 2 Movie")
        )
        coEvery { repository.getTrendingMovies(page, language) } returns expectedMovies

        val result = useCase(page, language)

        assertEquals(expectedMovies, result)
        assertEquals(1, result.size)
        coVerify { repository.getTrendingMovies(page, language) }
    }

    @Test
    fun `invoke with different language code passes it to repository`() = runTest {
        val page = 1
        val language = "es"
        val expectedMovies = listOf(
            createTestMovie(id = 1, title = "Película de moda")
        )
        coEvery { repository.getTrendingMovies(page, language) } returns expectedMovies

        val result = useCase(page, language)

        assertEquals(expectedMovies, result)
        coVerify { repository.getTrendingMovies(page, language) }
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runTest {
        val page = 1
        val language = "en"
        coEvery { repository.getTrendingMovies(page, language) } returns emptyList()

        val result = useCase(page, language)

        assertTrue(result.isEmpty())
        coVerify { repository.getTrendingMovies(page, language) }
    }

    @Test
    fun `invoke throws exception when repository throws exception`() = runTest {
        val page = 1
        val language = "en"
        val errorMessage = "Network error"
        coEvery { repository.getTrendingMovies(page, language) } throws Exception(errorMessage)

        var exceptionThrown = false
        try {
            useCase(page, language)
        } catch (e: Exception) {
            exceptionThrown = true
            assertEquals(errorMessage, e.message)
        }
        assertTrue("Expected exception was not thrown", exceptionThrown)
        coVerify { repository.getTrendingMovies(page, language) }
    }

    private fun createTestMovie(
        id: Int,
        title: String,
        posterPath: String = "/poster.jpg",
        backdropPath: String = "/backdrop.jpg",
        overview: String = "Test overview",
        releaseDate: String = "2024-01-01",
        voteAverage: Float = 8.5f,
        voteCount: Int = 1000,
        genreIds: List<Int> = listOf(28, 12),
        popularity: Float = 100.0f
    ) = Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        genreIds = genreIds,
        popularity = popularity,
        adult = false,
        video = false,
        originalTitle = title,
        originalLanguage = "en"
    )
}