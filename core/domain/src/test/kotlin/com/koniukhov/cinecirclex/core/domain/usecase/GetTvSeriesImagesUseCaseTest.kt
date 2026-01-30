package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.MediaImages
import com.koniukhov.cinecirclex.core.domain.model.Image
import com.koniukhov.cinecirclex.core.domain.repository.ImagesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTvSeriesImagesUseCaseTest {

    private lateinit var repository: ImagesRepository
    private lateinit var useCase: GetTvSeriesImagesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetTvSeriesImagesUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke returns tv series images from repository`() = runTest {
        val tvSeriesId = 1396
        val includeImageLanguage = "en"
        val expectedImages = MediaImages(
            id = 1396,
            backdrops = listOf(
                Image(
                    filePath = "/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg",
                    width = 1920,
                    height = 1080,
                    countryCode = "US",
                    aspectRatio = 1.78f,
                    voteAverage = 5.384f,
                    voteCount = 6
                ),
                Image(
                    filePath = "/eSzpy96DwBujGFj0xMbXBcGcfxX.jpg",
                    width = 1920,
                    height = 1080,
                    countryCode = "US",
                    aspectRatio = 1.78f,
                    voteAverage = 5.312f,
                    voteCount = 4
                )
            ),
            posters = listOf(
                Image(
                    filePath = "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
                    width = 2000,
                    height = 3000,
                    countryCode = "US",
                    aspectRatio = 0.67f,
                    voteAverage = 5.456f,
                    voteCount = 11
                )
            ),
            logos = emptyList()
        )

        coEvery { repository.getTvSeriesImages(tvSeriesId, includeImageLanguage) } returns expectedImages

        val result = useCase(tvSeriesId, includeImageLanguage)

        assertEquals(expectedImages, result)
        assertEquals(2, result.backdrops.size)
        assertEquals(1, result.posters.size)
        coVerify(exactly = 1) { repository.getTvSeriesImages(tvSeriesId, includeImageLanguage) }
    }

    @Test
    fun `invoke returns empty images when no images available`() = runTest {
        val tvSeriesId = 1396
        val includeImageLanguage = "en"
        val expectedImages = MediaImages.empty()

        coEvery { repository.getTvSeriesImages(tvSeriesId, includeImageLanguage) } returns expectedImages

        val result = useCase(tvSeriesId, includeImageLanguage)

        assertEquals(expectedImages, result)
        assertEquals(0, result.backdrops.size)
        assertEquals(0, result.posters.size)
        assertEquals(0, result.logos.size)
        coVerify(exactly = 1) { repository.getTvSeriesImages(tvSeriesId, includeImageLanguage) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val tvSeriesId = 1396
        val includeImageLanguage = "en"

        coEvery { repository.getTvSeriesImages(tvSeriesId, includeImageLanguage) } throws Exception("Network error")

        useCase(tvSeriesId, includeImageLanguage)
    }

    @Test
    fun `invoke handles different tv series ids correctly`() = runTest {
        val tvSeriesId = 1668
        val includeImageLanguage = "en"
        val expectedImages = MediaImages(
            id = 1668,
            backdrops = listOf(
                Image(
                    filePath = "/backdrop1.jpg",
                    width = 1920,
                    height = 1080,
                    countryCode = "US",
                    aspectRatio = 1.78f,
                    voteAverage = 5.0f,
                    voteCount = 10
                )
            ),
            posters = listOf(
                Image(
                    filePath = "/poster1.jpg",
                    width = 2000,
                    height = 3000,
                    countryCode = "US",
                    aspectRatio = 0.67f,
                    voteAverage = 5.5f,
                    voteCount = 15
                )
            ),
            logos = emptyList()
        )

        coEvery { repository.getTvSeriesImages(tvSeriesId, includeImageLanguage) } returns expectedImages

        val result = useCase(tvSeriesId, includeImageLanguage)

        assertEquals(expectedImages, result)
        coVerify(exactly = 1) { repository.getTvSeriesImages(tvSeriesId, includeImageLanguage) }
    }

    @Test
    fun `invoke handles images with logos`() = runTest {
        val tvSeriesId = 60059
        val includeImageLanguage = "en"
        val expectedImages = MediaImages(
            id = 60059,
            backdrops = emptyList(),
            posters = emptyList(),
            logos = listOf(
                Image(
                    filePath = "/logo1.png",
                    width = 500,
                    height = 200,
                    countryCode = "US",
                    aspectRatio = 2.5f,
                    voteAverage = 5.0f,
                    voteCount = 5
                ),
                Image(
                    filePath = "/logo2.png",
                    width = 600,
                    height = 240,
                    countryCode = "US",
                    aspectRatio = 2.5f,
                    voteAverage = 4.8f,
                    voteCount = 3
                )
            )
        )

        coEvery { repository.getTvSeriesImages(tvSeriesId, includeImageLanguage) } returns expectedImages

        val result = useCase(tvSeriesId, includeImageLanguage)

        assertEquals(expectedImages, result)
        assertEquals(0, result.backdrops.size)
        assertEquals(0, result.posters.size)
        assertEquals(2, result.logos.size)
        coVerify(exactly = 1) { repository.getTvSeriesImages(tvSeriesId, includeImageLanguage) }
    }
}