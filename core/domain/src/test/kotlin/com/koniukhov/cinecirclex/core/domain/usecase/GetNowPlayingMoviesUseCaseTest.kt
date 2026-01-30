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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GetNowPlayingMoviesUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetNowPlayingMoviesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetNowPlayingMoviesUseCase(repository)
    }

    @Test
    fun `invoke returns now playing movies from repository`() = runTest {
        val page = 1
        val language = "en"
        val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val expectedMovies = listOf(
            createTestMovie(id = 1, title = "Now Playing Movie 1", releaseDate = currentDate),
            createTestMovie(id = 2, title = "Now Playing Movie 2", releaseDate = currentDate)
        )

        coEvery { repository.getNowPlayingMovies(page, language) } returns expectedMovies

        val result = useCase(page, language)

        assertEquals(expectedMovies, result)
        assertEquals(2, result.size)
        assertEquals("Now Playing Movie 1", result[0].title)
        coVerify(exactly = 1) { repository.getNowPlayingMovies(page, language) }
    }

    @Test
    fun `invoke returns empty list when no movies are playing`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getNowPlayingMovies(page, language) } returns emptyList()

        val result = useCase(page, language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getNowPlayingMovies(page, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getNowPlayingMovies(page, language) } throws Exception("Network error")

        useCase(page, language)
    }

    @Test
    fun `invoke with different page numbers calls repository correctly`() = runTest {
        val page1 = 1
        val page2 = 2
        val language = "en"
        val movies = listOf<Movie>()

        coEvery { repository.getNowPlayingMovies(any(), any()) } returns movies

        useCase(page1, language)
        useCase(page2, language)

        coVerify(exactly = 1) { repository.getNowPlayingMovies(page1, language) }
        coVerify(exactly = 1) { repository.getNowPlayingMovies(page2, language) }
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val page = 1
        val languageEn = "en"
        val languageEs = "es"
        val movies = listOf<Movie>()

        coEvery { repository.getNowPlayingMovies(any(), any()) } returns movies

        useCase(page, languageEn)
        useCase(page, languageEs)

        coVerify(exactly = 1) { repository.getNowPlayingMovies(page, languageEn) }
        coVerify(exactly = 1) { repository.getNowPlayingMovies(page, languageEs) }
    }

    @Test
    fun `invoke returns movies with recent release dates`() = runTest {
        val page = 1
        val language = "en"
        val today = LocalDate.now()
        val movies = listOf(
            createTestMovie(id = 1, releaseDate = today.format(DateTimeFormatter.ISO_DATE)),
            createTestMovie(id = 2, releaseDate = today.minusDays(7).format(DateTimeFormatter.ISO_DATE)),
            createTestMovie(id = 3, releaseDate = today.minusDays(14).format(DateTimeFormatter.ISO_DATE))
        )

        coEvery { repository.getNowPlayingMovies(page, language) } returns movies

        val result = useCase(page, language)

        assertEquals(3, result.size)
        result.forEach { movie ->
            val releaseDate = LocalDate.parse(movie.releaseDate)
            assertTrue(releaseDate.isBefore(today.plusDays(1)))
        }
    }

    private fun createTestMovie(
        id: Int = 1,
        title: String = "Test Movie",
        releaseDate: String = "2024-01-01"
    ) = Movie(
        id = id,
        title = title,
        overview = "Test overview",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        releaseDate = releaseDate,
        voteAverage = 7.5f,
        voteCount = 100,
        popularity = 100.0f,
        adult = false,
        originalLanguage = "en",
        originalTitle = title,
        video = false,
        genreIds = listOf(28, 12)
    )
}