package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetTopRatedMoviesUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetTopRatedMoviesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetTopRatedMoviesUseCase(repository)
    }

    @Test
    fun `invoke returns flow of movies from repository`() = runTest {
        val page = 1
        val language = "en"
        val expectedMovies = listOf(
            Movie(
                id = 1,
                title = "The Shawshank Redemption",
                overview = "Two imprisoned men bond",
                posterPath = "/poster1.jpg",
                backdropPath = "/backdrop1.jpg",
                releaseDate = "1994-09-23",
                voteAverage = 8.7f,
                voteCount = 24000,
                popularity = 95.5f,
                adult = false,
                originalLanguage = "en",
                originalTitle = "The Shawshank Redemption",
                video = false,
                genreIds = listOf(18, 80)
            ),
            Movie(
                id = 2,
                title = "The Godfather",
                overview = "The aging patriarch",
                posterPath = "/poster2.jpg",
                backdropPath = "/backdrop2.jpg",
                releaseDate = "1972-03-14",
                voteAverage = 8.7f,
                voteCount = 18000,
                popularity = 92.3f,
                adult = false,
                originalLanguage = "en",
                originalTitle = "The Godfather",
                video = false,
                genreIds = listOf(18, 80)
            )
        )

        coEvery { repository.getTopRatedMovies(page, language) } returns expectedMovies

        val result = useCase(page, language)

        assertEquals(expectedMovies, result)
        assertEquals(2, result.size)
        coVerify(exactly = 1) { repository.getTopRatedMovies(page, language) }
    }

    @Test
    fun `invoke returns empty flow when repository returns empty list`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getTopRatedMovies(page, language) } returns emptyList()

        val result = useCase(page, language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getTopRatedMovies(page, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getTopRatedMovies(page, language) } throws Exception("Network error")

        useCase(page, language)
    }

    @Test
    fun `invoke with different page numbers calls repository correctly`() = runTest {
        val page1 = 1
        val page2 = 2
        val language = "en"
        val movies = listOf<Movie>()

        coEvery { repository.getTopRatedMovies(any(), any()) } returns movies

        val res1 = useCase(page1, language)
        val res2 = useCase(page2, language)

        assertNotNull(res1)
        assertNotNull(res2)

        coVerify(exactly = 1) { repository.getTopRatedMovies(page1, language) }
        coVerify(exactly = 1) { repository.getTopRatedMovies(page2, language) }
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val page = 1
        val languageEn = "en"
        val languageEs = "es"
        val movies = listOf<Movie>()

        coEvery { repository.getTopRatedMovies(any(), any()) } returns movies

        val res1 = useCase(page, languageEn)
        val res2 = useCase(page, languageEs)

        assertNotNull(res1)
        assertNotNull(res2)

        coVerify(exactly = 1) { repository.getTopRatedMovies(page, languageEn) }
        coVerify(exactly = 1) { repository.getTopRatedMovies(page, languageEs) }
    }
}