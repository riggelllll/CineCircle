package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.repository.GenresRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMovieGenresUseCaseTest {

    private lateinit var repository: GenresRepository
    private lateinit var useCase: GetMovieGenresUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMovieGenresUseCase(repository)
    }

    @Test
    fun `invoke returns list of genres from repository`() = runTest {
        val language = "en"
        val expectedGenres = listOf(
            Genre(id = 28, name = "Action"),
            Genre(id = 12, name = "Adventure"),
            Genre(id = 16, name = "Animation"),
            Genre(id = 35, name = "Comedy"),
            Genre(id = 80, name = "Crime"),
            Genre(id = 18, name = "Drama")
        )

        coEvery { repository.getMoviesGenreList(language) } returns expectedGenres

        val result = useCase(language)

        assertEquals(expectedGenres, result)
        assertEquals(6, result.size)
        coVerify(exactly = 1) { repository.getMoviesGenreList(language) }
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runTest {
        val language = "en"

        coEvery { repository.getMoviesGenreList(language) } returns emptyList()

        val result = useCase(language)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getMoviesGenreList(language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val language = "en"

        coEvery { repository.getMoviesGenreList(language) } throws Exception("Network error")

        useCase(language)
    }

    @Test
    fun `invoke verifies genre structure`() = runTest {
        val language = "en"
        val genres = listOf(
            Genre(id = 28, name = "Action"),
            Genre(id = 12, name = "Adventure")
        )

        coEvery { repository.getMoviesGenreList(language) } returns genres

        val result = useCase(language)

        assertEquals(2, result.size)
        assertEquals(28, result[0].id)
        assertEquals("Action", result[0].name)
        assertEquals(12, result[1].id)
        assertEquals("Adventure", result[1].name)
    }

    @Test
    fun `invoke returns all standard movie genres`() = runTest {
        val language = "en"
        val allGenres = listOf(
            Genre(id = 28, name = "Action"),
            Genre(id = 12, name = "Adventure"),
            Genre(id = 16, name = "Animation"),
            Genre(id = 35, name = "Comedy"),
            Genre(id = 80, name = "Crime"),
            Genre(id = 99, name = "Documentary"),
            Genre(id = 18, name = "Drama"),
            Genre(id = 10751, name = "Family"),
            Genre(id = 14, name = "Fantasy"),
            Genre(id = 36, name = "History"),
            Genre(id = 27, name = "Horror"),
            Genre(id = 10402, name = "Music"),
            Genre(id = 9648, name = "Mystery"),
            Genre(id = 10749, name = "Romance"),
            Genre(id = 878, name = "Science Fiction"),
            Genre(id = 10770, name = "TV Movie"),
            Genre(id = 53, name = "Thriller"),
            Genre(id = 10752, name = "War"),
            Genre(id = 37, name = "Western")
        )

        coEvery { repository.getMoviesGenreList(language) } returns allGenres

        val result = useCase(language)

        assertEquals(19, result.size)
        assertTrue(result.any { it.name == "Action" })
        assertTrue(result.any { it.name == "Comedy" })
        assertTrue(result.any { it.name == "Drama" })
        assertTrue(result.any { it.name == "Horror" })
        assertTrue(result.any { it.name == "Science Fiction" })
    }
}