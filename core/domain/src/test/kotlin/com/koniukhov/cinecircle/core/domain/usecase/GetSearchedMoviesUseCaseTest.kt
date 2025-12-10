package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetSearchedMoviesUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetSearchedMoviesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetSearchedMoviesUseCase(repository)
    }

    @Test
    fun `invoke returns list of searched movies from repository`() = runTest {
        val query = "Shawshank"
        val page = 1
        val language = "en"
        val expectedMovies = listOf(
            Movie(
                id = 278,
                title = "The Shawshank Redemption",
                overview = "Two imprisoned men bond",
                posterPath = "/poster.jpg",
                backdropPath = "/backdrop.jpg",
                releaseDate = "1994-09-23",
                voteAverage = 8.7f,
                voteCount = 24000,
                popularity = 95.5f,
                adult = false,
                originalLanguage = "en",
                originalTitle = "The Shawshank Redemption",
                video = false,
                genreIds = listOf(18, 80)
            )
        )

        coEvery { repository.getSearchedMovies(query, page, language) } returns expectedMovies

        val result = useCase(query, page, language)

        assertEquals(expectedMovies, result)
        assertEquals(1, result.size)
        assertEquals("The Shawshank Redemption", result[0].title)
        coVerify(exactly = 1) { repository.getSearchedMovies(query, page, language) }
    }

    @Test
    fun `invoke returns empty list when no movies match search query`() = runTest {
        val query = "NonExistentMovie123456"
        val page = 1
        val language = "en"

        coEvery { repository.getSearchedMovies(query, page, language) } returns emptyList()

        val result = useCase(query, page, language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getSearchedMovies(query, page, language) }
    }

    @Test
    fun `invoke returns empty flow when query is empty`() = runTest {
        val query = ""
        val page = 1
        val language = "en"

        coEvery { repository.getSearchedMovies(query, page, language) } returns emptyList()

        val result = useCase(query, page, language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getSearchedMovies(query, page, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val query = "Inception"
        val page = 1
        val language = "en"

        coEvery { repository.getSearchedMovies(query, page, language) } throws Exception("Network error")

        useCase(query, page, language)
    }

    @Test
    fun `invoke with different queries calls repository correctly`() = runTest {
        val query1 = "Matrix"
        val query2 = "Inception"
        val page = 1
        val language = "en"
        val movies = listOf<Movie>()

        coEvery { repository.getSearchedMovies(any(), any(), any()) } returns movies

        useCase(query1, page, language)
        useCase(query2, page, language)

        coVerify(exactly = 1) { repository.getSearchedMovies(query1, page, language) }
        coVerify(exactly = 1) { repository.getSearchedMovies(query2, page, language) }
    }

    @Test
    fun `invoke with different page numbers calls repository correctly`() = runTest {
        val query = "Matrix"
        val page1 = 1
        val page2 = 2
        val language = "en"
        val movies = listOf<Movie>()

        coEvery { repository.getSearchedMovies(any(), any(), any()) } returns movies

        useCase(query, page1, language)
        useCase(query, page2, language)

        coVerify(exactly = 1) { repository.getSearchedMovies(query, page1, language) }
        coVerify(exactly = 1) { repository.getSearchedMovies(query, page2, language) }
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val query = "Matrix"
        val page = 1
        val languageEn = "en"
        val languageEs = "es"
        val movies = listOf<Movie>()

        coEvery { repository.getSearchedMovies(any(), any(), any()) } returns movies

        useCase(query, page, languageEn)
        useCase(query, page, languageEs)

        coVerify(exactly = 1) { repository.getSearchedMovies(query, page, languageEn) }
        coVerify(exactly = 1) { repository.getSearchedMovies(query, page, languageEs) }
    }

    @Test
    fun `invoke returns multiple movies matching search query`() = runTest {
        val query = "Star"
        val page = 1
        val language = "en"
        val movies = listOf(
            Movie(
                id = 1,
                title = "Star Wars",
                overview = "A galaxy far away",
                posterPath = "/poster1.jpg",
                backdropPath = "/backdrop1.jpg",
                releaseDate = "1977-05-25",
                voteAverage = 8.6f,
                voteCount = 20000,
                popularity = 90.0f,
                adult = false,
                originalLanguage = "en",
                originalTitle = "Star Wars",
                video = false,
                genreIds = listOf(12, 878)
            ),
            Movie(
                id = 2,
                title = "Star Trek",
                overview = "Space exploration",
                posterPath = "/poster2.jpg",
                backdropPath = "/backdrop2.jpg",
                releaseDate = "2009-05-08",
                voteAverage = 7.9f,
                voteCount = 15000,
                popularity = 85.0f,
                adult = false,
                originalLanguage = "en",
                originalTitle = "Star Trek",
                video = false,
                genreIds = listOf(12, 878)
            )
        )

        coEvery { repository.getSearchedMovies(query, page, language) } returns movies

        val result = useCase(query, page, language)

        assertEquals(2, result.size)
        assertTrue(result[0].title.contains("Star"))
        assertTrue(result[1].title.contains("Star"))
    }

    @Test
    fun `invoke with special characters in query calls repository correctly`() = runTest {
        val query = "Spider-Man: No Way Home"
        val page = 1
        val language = "en"
        val movies = listOf<Movie>()

        coEvery { repository.getSearchedMovies(query, page, language) } returns movies

        val result = useCase(query, page, language)

        assertEquals(0, result.size)
        coVerify(exactly = 1) { repository.getSearchedMovies(query, page, language) }
    }
}