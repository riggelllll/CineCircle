package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.remote.RemoteImagesDataSourceImpl
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.network.model.ImageDto
import com.koniukhov.cinecircle.core.network.model.MediaImagesDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ImagesRepositoryImplTest {

    private lateinit var repository: ImagesRepositoryImpl
    private lateinit var remoteDataSource: RemoteImagesDataSourceImpl
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @Before
    fun setup() {
        remoteDataSource = mockk()
        networkStatusProvider = mockk()

        repository = ImagesRepositoryImpl(
            remoteImagesDataSourceImpl = remoteDataSource,
            networkStatusProvider = networkStatusProvider
        )
    }

    @Test
    fun `getMovieImages should return images when network is available`() = runTest {
        val backdropDto = ImageDto(
            filePath = "/backdrop.jpg",
            aspectRatio = 1.78f,
            height = 1080,
            width = 1920,
            voteAverage = 7.5f,
            voteCount = 100,
            countryCode = null
        )
        val posterDto = ImageDto(
            filePath = "/poster.jpg",
            aspectRatio = 0.67f,
            height = 1500,
            width = 1000,
            voteAverage = 8.0f,
            voteCount = 150,
            countryCode = null
        )
        val imagesDto = MediaImagesDto(
            id = 100,
            backdrops = listOf(backdropDto),
            logos = emptyList(),
            posters = listOf(posterDto)
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieImages(any(), any()) } returns imagesDto

        val result = repository.getMovieImages(movieId = 100, language = "en")

        assertEquals(1, result.backdrops.size)
        assertEquals("/backdrop.jpg", result.backdrops[0].filePath)
        assertEquals(1, result.posters.size)
        assertEquals("/poster.jpg", result.posters[0].filePath)
        coVerify { remoteDataSource.getMovieImages(100, "en") }
    }

    @Test
    fun `getMovieImages should return empty images when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieImages(any(), any()) } throws Exception("Network error")

        val result = repository.getMovieImages(movieId = 100, language = "en")

        assertTrue(result.backdrops.isEmpty())
        assertTrue(result.posters.isEmpty())
    }

    @Test
    fun `getTvSeriesImages should return images when network is available`() = runTest {
        val backdropDto = ImageDto(
            filePath = "/tv_backdrop.jpg",
            aspectRatio = 1.78f,
            height = 1080,
            width = 1920,
            voteAverage = 7.0f,
            voteCount = 80,
            countryCode = null
        )
        val posterDto = ImageDto(
            filePath = "/tv_poster.jpg",
            aspectRatio = 0.67f,
            height = 1500,
            width = 1000,
            voteAverage = 8.5f,
            voteCount = 200,
            countryCode = null
        )
        val imagesDto = MediaImagesDto(
            id = 200,
            backdrops = listOf(backdropDto),
            logos = emptyList(),
            posters = listOf(posterDto)
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesImages(any(), any()) } returns imagesDto

        val result = repository.getTvSeriesImages(tvSeriesId = 200, language = "en")

        assertEquals(1, result.backdrops.size)
        assertEquals("/tv_backdrop.jpg", result.backdrops[0].filePath)
        assertEquals(1, result.posters.size)
        assertEquals("/tv_poster.jpg", result.posters[0].filePath)
        coVerify { remoteDataSource.getTvSeriesImages(200, "en") }
    }

    @Test
    fun `getTvSeriesImages should return empty images when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesImages(any(), any()) } throws Exception("Network error")

        val result = repository.getTvSeriesImages(tvSeriesId = 200, language = "en")

        assertTrue(result.backdrops.isEmpty())
        assertTrue(result.posters.isEmpty())
    }

    @Test
    fun `getMovieImages should handle empty backdrops and posters`() = runTest {
        val imagesDto = MediaImagesDto(
            id = 100,
            backdrops = emptyList(),
            logos = emptyList(),
            posters = emptyList()
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieImages(any(), any()) } returns imagesDto

        val result = repository.getMovieImages(movieId = 100, language = "en")

        assertTrue(result.backdrops.isEmpty())
        assertTrue(result.posters.isEmpty())
    }

    @Test
    fun `getTvSeriesImages should handle empty backdrops and posters`() = runTest {
        val imagesDto = MediaImagesDto(
            id = 200,
            backdrops = emptyList(),
            logos = emptyList(),
            posters = emptyList()
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesImages(any(), any()) } returns imagesDto

        val result = repository.getTvSeriesImages(tvSeriesId = 200, language = "en")

        assertTrue(result.backdrops.isEmpty())
        assertTrue(result.posters.isEmpty())
    }
}