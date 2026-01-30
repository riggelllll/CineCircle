package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.Movie
import com.koniukhov.cinecirclex.core.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMoviesByGenreUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetMoviesByGenreUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMoviesByGenreUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke calls repository with correct parameters and returns result`() = runTest {
        val genreId = 28
        val page = 1
        val language = "en"
        val expectedMovies = listOf(
            Movie(
                adult = false,
                backdropPath = "/backdrop.jpg",
                genreIds = listOf(28, 12),
                id = 123,
                originalLanguage = "en",
                originalTitle = "Action Movie",
                overview = "An action-packed movie",
                popularity = 75.5f,
                posterPath = "/poster.jpg",
                releaseDate = "2024-01-01",
                title = "Action Movie",
                video = false,
                voteAverage = 8.0f,
                voteCount = 1000
            )
        )
        coEvery { repository.getMoviesByGenre(genreId, page, language) } returns expectedMovies

        val result = useCase(genreId, page, language)

        assertNotNull(result)
        assertEquals(expectedMovies, result)
        assertEquals(1, result.size)
        assertEquals("Action Movie", result[0].title)
        assertTrue(result[0].genreIds.contains(28))
        coVerify(exactly = 1) { repository.getMoviesByGenre(genreId, page, language) }
    }

    @Test
    fun `invoke propagates exception from repository`() = runTest {
        val genreId = 28
        val page = 1
        val language = "en"
        val exception = RuntimeException("Network error")
        coEvery { repository.getMoviesByGenre(genreId, page, language) } throws exception

        try {
            useCase(genreId, page, language)
            throw AssertionError("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Network error", e.message)
        }

        coVerify { repository.getMoviesByGenre(genreId, page, language) }
    }

    @Test
    fun `invoke returns empty list when no movies found for genre`() = runTest {
        val genreId = 999
        val page = 1
        val language = "en"
        val emptyList = emptyList<Movie>()
        coEvery { repository.getMoviesByGenre(genreId, page, language) } returns emptyList

        val result = useCase(genreId, page, language)

        assertTrue(result.isEmpty())
        coVerify { repository.getMoviesByGenre(genreId, page, language) }
    }

    @Test
    fun `invoke with different genre IDs returns different movies`() = runTest {
        val actionGenre = 28
        val comedyGenre = 35
        val page = 1
        val language = "en"
        val actionMovies = listOf(Movie(false, "", listOf(28), 1, "", "", "", 0f, "", "", "Action 1", false, 0f, 0))
        val comedyMovies = listOf(Movie(false, "", listOf(35), 2, "", "", "", 0f, "", "", "Comedy 1", false, 0f, 0))
        coEvery { repository.getMoviesByGenre(actionGenre, page, language) } returns actionMovies
        coEvery { repository.getMoviesByGenre(comedyGenre, page, language) } returns comedyMovies

        val actionResult = useCase(actionGenre, page, language)
        val comedyResult = useCase(comedyGenre, page, language)

        assertEquals("Action 1", actionResult[0].title)
        assertEquals("Comedy 1", comedyResult[0].title)
        coVerify { repository.getMoviesByGenre(actionGenre, page, language) }
        coVerify { repository.getMoviesByGenre(comedyGenre, page, language) }
    }

    @Test
    fun `invoke with different page numbers returns paginated results`() = runTest {
        val genreId = 28
        val page1 = 1
        val page2 = 2
        val language = "en"
        val movies1 = listOf(Movie(false, "", listOf(), 1, "", "", "", 0f, "", "", "Movie 1", false, 0f, 0))
        val movies2 = listOf(Movie(false, "", listOf(), 2, "", "", "", 0f, "", "", "Movie 2", false, 0f, 0))
        coEvery { repository.getMoviesByGenre(genreId, page1, language) } returns movies1
        coEvery { repository.getMoviesByGenre(genreId, page2, language) } returns movies2

        val result1 = useCase(genreId, page1, language)
        val result2 = useCase(genreId, page2, language)

        assertEquals(1, result1[0].id)
        assertEquals(2, result2[0].id)
        coVerify { repository.getMoviesByGenre(genreId, page1, language) }
        coVerify { repository.getMoviesByGenre(genreId, page2, language) }
    }
}