package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.repository.GenresRepository
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

class GetMovieGenresUseCaseTest {

    private lateinit var repository: GenresRepository
    private lateinit var useCase: GetMovieGenresUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMovieGenresUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke calls repository with correct language and returns result`() = runTest {
        val language = "en"
        val expectedGenres = listOf(
            Genre(id = 28, name = "Action"),
            Genre(id = 12, name = "Adventure"),
            Genre(id = 35, name = "Comedy")
        )
        coEvery { repository.getMoviesGenreList(language) } returns expectedGenres

        val result = useCase(language)

        assertEquals(expectedGenres, result)
        assertEquals(3, result.size)
        assertEquals("Action", result[0].name)
        assertEquals(28, result[0].id)
        coVerify(exactly = 1) { repository.getMoviesGenreList(language) }
    }

    @Test
    fun `invoke with different language returns localized genres`() = runTest {
        val language = "es"
        val expectedGenres = listOf(
            Genre(id = 28, name = "Acción"),
            Genre(id = 12, name = "Aventura")
        )
        coEvery { repository.getMoviesGenreList(language) } returns expectedGenres

        val result = useCase(language)

        assertEquals(expectedGenres, result)
        assertEquals("Acción", result[0].name)
        coVerify { repository.getMoviesGenreList(language) }
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runTest {
        val language = "en"
        coEvery { repository.getMoviesGenreList(language) } returns emptyList()

        val result = useCase(language)

        assertTrue(result.isEmpty())
        coVerify { repository.getMoviesGenreList(language) }
    }

    @Test
    fun `invoke throws exception when repository throws exception`() = runTest {
        val language = "en"
        val errorMessage = "Network error"
        coEvery { repository.getMoviesGenreList(language) } throws Exception(errorMessage)

        var exceptionThrown = false
        try {
            useCase(language)
        } catch (e: Exception) {
            exceptionThrown = true
            assertEquals(errorMessage, e.message)
        }
        assertTrue("Expected exception was not thrown", exceptionThrown)
        coVerify { repository.getMoviesGenreList(language) }
    }
}