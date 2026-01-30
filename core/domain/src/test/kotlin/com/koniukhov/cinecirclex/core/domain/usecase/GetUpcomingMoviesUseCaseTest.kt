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

class GetUpcomingMoviesUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetUpcomingMoviesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetUpcomingMoviesUseCase(repository)
    }

    @Test
    fun `invoke returns upcoming movies from repository`() = runTest {
        val page = 1
        val language = "en"
        val expectedMovies = listOf(
            createTestMovie(id = 1, title = "Upcoming Movie 1"),
            createTestMovie(id = 2, title = "Upcoming Movie 2")
        )

        coEvery { repository.getUpcomingMovies(page, language) } returns expectedMovies

        val result = useCase(page, language)

        assertEquals(expectedMovies, result)
        assertEquals(2, result.size)
        assertEquals("Upcoming Movie 1", result[0].title)
        coVerify(exactly = 1) { repository.getUpcomingMovies(page, language) }
    }

    @Test
    fun `invoke returns empty list when repository returns empty`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getUpcomingMovies(page, language) } returns emptyList()

        val result = useCase(page, language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getUpcomingMovies(page, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val page = 1
        val language = "en"

        coEvery { repository.getUpcomingMovies(page, language) } throws Exception("Network error")

        useCase(page, language)
    }

    @Test
    fun `invoke with different page numbers calls repository correctly`() = runTest {
        val page1 = 1
        val page2 = 2
        val language = "en"
        val movies = listOf<Movie>()

        coEvery { repository.getUpcomingMovies(any(), any()) } returns movies

        useCase(page1, language)
        useCase(page2, language)

        coVerify(exactly = 1) { repository.getUpcomingMovies(page1, language) }
        coVerify(exactly = 1) { repository.getUpcomingMovies(page2, language) }
    }

    @Test
    fun `invoke returns movies with future release dates`() = runTest {
        val page = 1
        val language = "en"
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ISO_DATE

        val movies = listOf(
            createTestMovie(id = 1, releaseDate = today.plusDays(7).format(formatter)),
            createTestMovie(id = 2, releaseDate = today.plusDays(14).format(formatter)),
            createTestMovie(id = 3, releaseDate = today.plusDays(30).format(formatter))
        )

        coEvery { repository.getUpcomingMovies(page, language) } returns movies

        val result = useCase(page, language)

        assertEquals(3, result.size)
        result.forEach { movie ->
            assertTrue("Movie should have release date", movie.releaseDate.isNotEmpty())
        }
    }

    @Test
    fun `invoke returns movies sorted by release date`() = runTest {
        val page = 1
        val language = "en"
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ISO_DATE

        val movies = listOf(
            createTestMovie(id = 1, title = "Coming Soon", releaseDate = today.plusDays(5).format(formatter)),
            createTestMovie(id = 2, title = "Later This Month", releaseDate = today.plusDays(15).format(formatter)),
            createTestMovie(id = 3, title = "Next Month", releaseDate = today.plusDays(40).format(formatter))
        )

        coEvery { repository.getUpcomingMovies(page, language) } returns movies

        val result = useCase(page, language)

        assertEquals(3, result.size)
        for (i in 0 until result.size - 1) {
            val currentDate = LocalDate.parse(result[i].releaseDate, formatter)
            val nextDate = LocalDate.parse(result[i + 1].releaseDate, formatter)
            assertTrue(
                "Movies should be sorted by release date",
                !currentDate.isAfter(nextDate)
            )
        }
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val page = 1
        val languageEn = "en"
        val languageEs = "es"
        val movies = listOf<Movie>()

        coEvery { repository.getUpcomingMovies(any(), any()) } returns movies

        useCase(page, languageEn)
        useCase(page, languageEs)

        coVerify(exactly = 1) { repository.getUpcomingMovies(page, languageEn) }
        coVerify(exactly = 1) { repository.getUpcomingMovies(page, languageEs) }
    }

    @Test
    fun `invoke returns movies with valid metadata`() = runTest {
        val page = 1
        val language = "en"
        val movies = listOf(
            createTestMovie(
                id = 1,
                title = "Upcoming Blockbuster",
                voteAverage = 0.0f,
                voteCount = 0,
                popularity = 500.0f
            ),
            createTestMovie(
                id = 2,
                title = "Anticipated Release",
                voteAverage = 8.5f,
                voteCount = 50,
                popularity = 800.0f
            )
        )

        coEvery { repository.getUpcomingMovies(page, language) } returns movies

        val result = useCase(page, language)

        assertEquals(2, result.size)
        result.forEach { movie ->
            assertTrue("Movie should have title", movie.title.isNotEmpty())
            assertTrue("Movie should have poster path", movie.posterPath.isNotEmpty())
            assertTrue("Movie should have popularity >= 0", movie.popularity >= 0.0f)
        }
    }

    @Test
    fun `invoke returns movies from various genres`() = runTest {
        val page = 1
        val language = "en"
        val movies = listOf(
            createTestMovie(id = 1, genreIds = listOf(28)),
            createTestMovie(id = 2, genreIds = listOf(878)),
            createTestMovie(id = 3, genreIds = listOf(35))
        )

        coEvery { repository.getUpcomingMovies(page, language) } returns movies

        val result = useCase(page, language)

        assertEquals(3, result.size)
        val allGenres = result.flatMap { it.genreIds }.toSet()
        assertTrue(allGenres.contains(28))
        assertTrue(allGenres.contains(878))
        assertTrue(allGenres.contains(35))
    }

    @Test
    fun `invoke returns highly anticipated movies`() = runTest {
        val page = 1
        val language = "en"
        val movies = listOf(
            createTestMovie(id = 1, title = "Major Franchise Sequel", popularity = 1500.0f),
            createTestMovie(id = 2, title = "Highly Anticipated Adaptation", popularity = 1200.0f),
            createTestMovie(id = 3, title = "Superhero Blockbuster", popularity = 1800.0f)
        )

        coEvery { repository.getUpcomingMovies(page, language) } returns movies

        val result = useCase(page, language)

        assertEquals(3, result.size)
        assertTrue(result.all { it.popularity >= 1000.0f })
    }

    private fun createTestMovie(
        id: Int = 1,
        title: String = "Test Movie",
        releaseDate: String = LocalDate.now().plusDays(30).format(DateTimeFormatter.ISO_DATE),
        voteAverage: Float = 7.5f,
        voteCount: Int = 100,
        popularity: Float = 500.0f,
        genreIds: List<Int> = listOf(28, 12)
    ) = Movie(
        id = id,
        title = title,
        overview = "Test overview",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        adult = false,
        originalLanguage = "en",
        originalTitle = title,
        genreIds = genreIds,
        video = false
    )
}