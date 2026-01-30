package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.Movie
import com.koniukhov.cinecirclex.core.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMovieRecommendationsUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetMovieRecommendationsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMovieRecommendationsUseCase(repository)
    }

    @Test
    fun `invoke returns recommended movies from repository`() = runTest {
        val movieId = 278
        val page = 1
        val language = "en"
        val expectedMovies = listOf(
            createTestMovie(id = 1, title = "Recommended Movie 1"),
            createTestMovie(id = 2, title = "Recommended Movie 2")
        )

        coEvery { repository.getMovieRecommendations(movieId, page, language) } returns expectedMovies

        val result = useCase(movieId, page, language)

        assertEquals(expectedMovies, result)
        assertEquals(2, result.size)
        assertEquals("Recommended Movie 1", result[0].title)
        coVerify(exactly = 1) { repository.getMovieRecommendations(movieId, page, language) }
    }

    @Test
    fun `invoke returns empty list when no recommendations available`() = runTest {
        val movieId = 278
        val page = 1
        val language = "en"

        coEvery { repository.getMovieRecommendations(movieId, page, language) } returns emptyList()

        val result = useCase(movieId, page, language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getMovieRecommendations(movieId, page, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val movieId = 278
        val page = 1
        val language = "en"

        coEvery {
            repository.getMovieRecommendations(movieId, page, language)
        } throws Exception("Network error")

        useCase(movieId, page, language)
    }

    @Test
    fun `invoke with different movie IDs calls repository correctly`() = runTest {
        val movieId1 = 278
        val movieId2 = 238
        val page = 1
        val language = "en"
        val movies = listOf<Movie>()

        coEvery { repository.getMovieRecommendations(any(), any(), any()) } returns movies

        useCase(movieId1, page, language)
        useCase(movieId2, page, language)

        coVerify(exactly = 1) { repository.getMovieRecommendations(movieId1, page, language) }
        coVerify(exactly = 1) { repository.getMovieRecommendations(movieId2, page, language) }
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val movieId = 278
        val page = 1
        val languageEn = "en"
        val languageEs = "es"
        val movies = listOf<Movie>()

        coEvery { repository.getMovieRecommendations(any(), any(), any()) } returns movies

        useCase(movieId, page, languageEn)
        useCase(movieId, page, languageEs)

        coVerify(exactly = 1) { repository.getMovieRecommendations(movieId, page, languageEn) }
        coVerify(exactly = 1) { repository.getMovieRecommendations(movieId, page, languageEs) }
    }

    @Test
    fun `invoke returns movies with similar genres`() = runTest {
        val movieId = 278
        val page = 1
        val language = "en"
        val genreIds = listOf(18, 80)
        val movies = listOf(
            createTestMovie(id = 1, genreIds = listOf(18, 80)),
            createTestMovie(id = 2, genreIds = listOf(18, 53)),
            createTestMovie(id = 3, genreIds = listOf(80, 53))
        )

        coEvery { repository.getMovieRecommendations(movieId, page, language) } returns movies

        val result = useCase(movieId, page, language)

        assertEquals(3, result.size)
        result.forEach { movie ->
            assertTrue(movie.genreIds.any { genreIds.contains(it) })
        }
    }

    @Test
    fun `invoke with invalid movie ID returns empty list`() = runTest {
        val invalidMovieId = -1
        val page = 1
        val language = "en"

        coEvery {
            repository.getMovieRecommendations(invalidMovieId, page, language)
        } returns emptyList()

        val result = useCase(invalidMovieId, page, language)

        assertTrue(result.isEmpty())
    }

    private fun createTestMovie(
        id: Int = 1,
        title: String = "Test Movie",
        genreIds: List<Int> = listOf(28, 12)
    ) = Movie(
        id = id,
        title = title,
        overview = "Test overview",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        releaseDate = "2024-01-01",
        voteAverage = 7.5f,
        voteCount = 100,
        popularity = 100.0f,
        adult = false,
        originalLanguage = "en",
        originalTitle = title,
        video = false,
        genreIds = genreIds
    )
}