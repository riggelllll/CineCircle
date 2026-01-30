package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.Image
import com.koniukhov.cinecirclex.core.domain.model.MediaImages
import com.koniukhov.cinecirclex.core.domain.repository.ImagesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMovieImagesUseCaseTest {

    private lateinit var repository: ImagesRepository
    private lateinit var useCase: GetMovieImagesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMovieImagesUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke calls repository with correct parameters and returns result`() = runTest {
        val movieId = 123
        val language = "en"
        val expectedImages = MediaImages(
            id = movieId,
            backdrops = listOf(
                Image(
                    aspectRatio = 1.78f,
                    height = 1080,
                    filePath = "/backdrop.jpg",
                    voteAverage = 8.5f,
                    voteCount = 100,
                    width = 1920,
                    countryCode = "en"
                )
            ),
            logos = emptyList(),
            posters = emptyList()
        )
        coEvery { repository.getMovieImages(movieId, language) } returns expectedImages

        val result = useCase(movieId, language)

        assertNotNull(result)
        assertEquals(expectedImages, result)
        assertEquals(movieId, result.id)
        assertEquals(1, result.backdrops.size)
        coVerify(exactly = 1) { repository.getMovieImages(movieId, language) }
    }

    @Test
    fun `invoke propagates exception from repository`() = runTest {
        val movieId = 123
        val language = "en"
        val exception = RuntimeException("Network error")
        coEvery { repository.getMovieImages(movieId, language) } throws exception

        try {
            useCase(movieId, language)
            throw AssertionError("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Network error", e.message)
        }

        coVerify { repository.getMovieImages(movieId, language) }
    }

    @Test
    fun `invoke returns empty images when repository returns empty`() = runTest {
        val movieId = 999
        val language = "en"
        val emptyImages = MediaImages.empty()
        coEvery { repository.getMovieImages(movieId, language) } returns emptyImages

        val result = useCase(movieId, language)

        assertEquals(emptyImages, result)
        assertTrue(result.backdrops.isEmpty())
        assertTrue(result.logos.isEmpty())
        assertTrue(result.posters.isEmpty())
        coVerify { repository.getMovieImages(movieId, language) }
    }

    @Test
    fun `invoke returns images with all types populated`() = runTest {
        val movieId = 550
        val language = "en"
        val imagesWithAll = MediaImages(
            id = movieId,
            backdrops = listOf(
                Image(
                    aspectRatio = 1.78f,
                    height = 1080,
                    filePath = "/backdrop1.jpg",
                    voteAverage = 8.0f,
                    voteCount = 50,
                    width = 1920,
                    countryCode = "en"
                ),
                Image(
                    aspectRatio = 1.78f,
                    height = 720,
                    filePath = "/backdrop2.jpg",
                    voteAverage = 7.5f,
                    voteCount = 30,
                    width = 1280,
                    countryCode = "en"
                )
            ),
            logos = listOf(
                Image(
                    aspectRatio = 1.0f,
                    height = 500,
                    filePath = "/logo1.png",
                    voteAverage = 9.0f,
                    voteCount = 20,
                    width = 500,
                    countryCode = "en"
                )
            ),
            posters = listOf(
                Image(
                    aspectRatio = 0.67f,
                    height = 1500,
                    filePath = "/poster1.jpg",
                    voteAverage = 8.8f,
                    voteCount = 100,
                    width = 1000,
                    countryCode = "en"
                ),
                Image(
                    aspectRatio = 0.67f,
                    height = 1200,
                    filePath = "/poster2.jpg",
                    voteAverage = 8.2f,
                    voteCount = 80,
                    width = 800,
                    countryCode = "en"
                )
            )
        )
        coEvery { repository.getMovieImages(movieId, language) } returns imagesWithAll

        val result = useCase(movieId, language)

        assertEquals(2, result.backdrops.size)
        assertEquals(1, result.logos.size)
        assertEquals(2, result.posters.size)
        coVerify { repository.getMovieImages(movieId, language) }
    }

    @Test
    fun `invoke with different movie ID returns corresponding images`() = runTest {
        val movieId = 456
        val language = "en"
        val expectedImages = MediaImages.empty().copy(id = movieId)
        coEvery { repository.getMovieImages(movieId, language) } returns expectedImages

        val result = useCase(movieId, language)

        assertEquals(movieId, result.id)
        coVerify { repository.getMovieImages(movieId, language) }
    }
}