package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.MovieDetails
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class GetMovieDetailsUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetMovieDetailsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMovieDetailsUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke calls repository with correct parameters and returns result`() = runTest {
        val movieId = 123
        val language = "en"
        val expectedDetails = MovieDetails.empty().copy(
            id = movieId,
            title = "Test Movie",
            overview = "Test overview"
        )
        coEvery { repository.getMovieDetails(movieId, language) } returns expectedDetails
        val result = useCase(movieId, language)

        assertNotNull(result)
        assertEquals(expectedDetails, result)
        assertEquals(movieId, result.id)
        assertEquals("Test Movie", result.title)
        coVerify(exactly = 1) { repository.getMovieDetails(movieId, language) }
    }

    @Test
    fun `invoke with different movie ID returns corresponding details`() = runTest {
        val movieId = 456
        val language = "en"
        val expectedDetails = MovieDetails.empty().copy(
            id = movieId,
            title = "Another Movie"
        )
        coEvery { repository.getMovieDetails(movieId, language) } returns expectedDetails

        val result = useCase(movieId, language)

        assertEquals(expectedDetails, result)
        assertEquals(movieId, result.id)
        coVerify { repository.getMovieDetails(movieId, language) }
    }

    @Test
    fun `invoke with different language code passes it to repository`() = runTest {
        val movieId = 123
        val language = "es"
        val expectedDetails = MovieDetails.empty().copy(
            id = movieId,
            originalLanguage = language
        )
        coEvery { repository.getMovieDetails(movieId, language) } returns expectedDetails

        val result = useCase(movieId, language)

        assertEquals(expectedDetails, result)
        coVerify { repository.getMovieDetails(movieId, language) }
    }

    @Test
    fun `invoke propagates exception from repository`() = runTest {
        val movieId = 123
        val language = "en"
        val exception = RuntimeException("Network error")
        coEvery { repository.getMovieDetails(movieId, language) } throws exception

        try {
            useCase(movieId, language)
            throw AssertionError("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Network error", e.message)
        }

        coVerify { repository.getMovieDetails(movieId, language) }
    }

    @Test
    fun `invoke returns empty details when repository returns empty`() = runTest {
        val movieId = 999
        val language = "en"
        val emptyDetails = MovieDetails.empty()
        coEvery { repository.getMovieDetails(movieId, language) } returns emptyDetails

        val result = useCase(movieId, language)

        assertEquals(emptyDetails, result)
        assertEquals(-1, result.id)
        assertEquals("", result.title)
        coVerify { repository.getMovieDetails(movieId, language) }
    }

    @Test
    fun `invoke with zero movie ID still calls repository`() = runTest {
        val movieId = 0
        val language = "en"
        val expectedDetails = MovieDetails.empty()
        coEvery { repository.getMovieDetails(movieId, language) } returns expectedDetails

        val result = useCase(movieId, language)

        assertNotNull(result)
        coVerify { repository.getMovieDetails(movieId, language) }
    }

    @Test
    fun `invoke with negative movie ID still calls repository`() = runTest {
        val movieId = -1
        val language = "en"
        val expectedDetails = MovieDetails.empty()
        coEvery { repository.getMovieDetails(movieId, language) } returns expectedDetails

        val result = useCase(movieId, language)

        assertNotNull(result)
        coVerify { repository.getMovieDetails(movieId, language) }
    }

    @Test
    fun `invoke with empty language string passes it to repository`() = runTest {
        val movieId = 123
        val language = ""
        val expectedDetails = MovieDetails.empty()
        coEvery { repository.getMovieDetails(movieId, language) } returns expectedDetails

        val result = useCase(movieId, language)

        assertNotNull(result)
        coVerify { repository.getMovieDetails(movieId, language) }
    }

    @Test
    fun `invoke returns complete movie details with all fields populated`() = runTest {
        val movieId = 550
        val language = "en"
        val completeDetails = MovieDetails.empty().copy(
            id = movieId,
            title = "Fight Club",
            originalTitle = "Fight Club",
            overview = "A ticking-time-bomb insomniac...",
            releaseDate = "1999-10-15",
            runtime = 139,
            voteAverage = 8.4f,
            voteCount = 25000,
            popularity = 65.3f,
            adult = false,
            video = false,
            budget = 63000000,
            revenue = 100853753L,
            status = "Released",
            tagline = "Mischief. Mayhem. Soap.",
            originalLanguage = "en",
            posterPath = "/path/to/poster.jpg",
            backdropPath = "/path/to/backdrop.jpg"
        )
        coEvery { repository.getMovieDetails(movieId, language) } returns completeDetails

        val result = useCase(movieId, language)

        assertEquals(550, result.id)
        assertEquals("Fight Club", result.title)
        assertEquals("1999-10-15", result.releaseDate)
        assertEquals(139, result.runtime)
        assertEquals(8.4f, result.voteAverage)
        assertEquals(25000, result.voteCount)
        assertEquals(63000000, result.budget)
        assertEquals(100853753L, result.revenue)
        coVerify { repository.getMovieDetails(movieId, language) }
    }

    @Test
    fun `invoke can be called multiple times with same parameters`() = runTest {
        val movieId = 123
        val language = "en"
        val expectedDetails = MovieDetails.empty().copy(id = movieId)
        coEvery { repository.getMovieDetails(movieId, language) } returns expectedDetails

        val result1 = useCase(movieId, language)
        val result2 = useCase(movieId, language)

        assertEquals(result1, result2)
        coVerify(exactly = 2) { repository.getMovieDetails(movieId, language) }
    }

    @Test
    fun `invoke with different locales returns localized content`() = runTest {
        val movieId = 123
        val languageEn = "en"
        val languageUk = "uk"
        val detailsEn = MovieDetails.empty().copy(
            id = movieId,
            title = "The Matrix"
        )
        val detailsUk = MovieDetails.empty().copy(
            id = movieId,
            title = "Матриця"
        )
        coEvery { repository.getMovieDetails(movieId, languageEn) } returns detailsEn
        coEvery { repository.getMovieDetails(movieId, languageUk) } returns detailsUk

        val resultEn = useCase(movieId, languageEn)
        val resultUk = useCase(movieId, languageUk)

        assertEquals("The Matrix", resultEn.title)
        assertEquals("Матриця", resultUk.title)
        coVerify { repository.getMovieDetails(movieId, languageEn) }
        coVerify { repository.getMovieDetails(movieId, languageUk) }
    }
}