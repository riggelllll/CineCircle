package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.remote.RemoteVideosDataSourceImpl
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.network.model.MediaVideosDto
import com.koniukhov.cinecircle.core.network.model.VideoDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VideosRepositoryImplTest {

    private lateinit var repository: VideosRepositoryImpl
    private lateinit var remoteDataSource: RemoteVideosDataSourceImpl
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @Before
    fun setup() {
        remoteDataSource = mockk()
        networkStatusProvider = mockk()

        repository = VideosRepositoryImpl(
            remoteVideosDataSourceImpl = remoteDataSource,
            networkStatusProvider = networkStatusProvider
        )
    }

    @Test
    fun `getMovieVideos should return videos when network is available`() = runTest {
        val videoDto = VideoDto(
            id = "video1",
            key = "abc123",
            name = "Official Trailer",
            site = "YouTube",
            size = 1080,
            type = "Trailer",
            official = true,
            publishedAt = "2023-01-01T00:00:00Z",
            languageCode = "en",
            countryCode = "US"
        )
        val videosDto = MediaVideosDto(id = 100, results = listOf(videoDto))

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieVideos(any(), any()) } returns videosDto

        val result = repository.getMovieVideos(movieId = 100, language = "en")

        assertEquals(1, result.results.size)
        assertEquals("video1", result.results[0].id)
        assertEquals("abc123", result.results[0].key)
        assertEquals("Official Trailer", result.results[0].name)
        coVerify { remoteDataSource.getMovieVideos(100, "en") }
    }

    @Test
    fun `getMovieVideos should return empty videos when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieVideos(any(), any()) } throws Exception("Network error")

        val result = repository.getMovieVideos(movieId = 100, language = "en")

        assertTrue(result.results.isEmpty())
    }

    @Test
    fun `getTvSeriesVideos should return videos when network is available`() = runTest {
        val videoDto = VideoDto(
            id = "video2",
            key = "xyz789",
            name = "Season 1 Trailer",
            site = "YouTube",
            size = 720,
            type = "Trailer",
            official = true,
            publishedAt = "2023-02-01T00:00:00Z",
            languageCode = "en",
            countryCode = "US"
        )
        val videosDto = MediaVideosDto(id = 200, results = listOf(videoDto))

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesVideos(any(), any()) } returns videosDto

        val result = repository.getTvSeriesVideos(tvSeriesId = 200, language = "en")

        assertEquals(1, result.results.size)
        assertEquals("video2", result.results[0].id)
        assertEquals("xyz789", result.results[0].key)
        assertEquals("Season 1 Trailer", result.results[0].name)
        coVerify { remoteDataSource.getTvSeriesVideos(200, "en") }
    }

    @Test
    fun `getTvSeriesVideos should return empty videos when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesVideos(any(), any()) } throws Exception("Network error")

        val result = repository.getTvSeriesVideos(tvSeriesId = 200, language = "en")

        assertTrue(result.results.isEmpty())
    }

    @Test
    fun `getMovieVideos should handle empty results`() = runTest {
        val videosDto = MediaVideosDto(id = 100, results = emptyList())

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieVideos(any(), any()) } returns videosDto

        val result = repository.getMovieVideos(movieId = 100, language = "en")

        assertTrue(result.results.isEmpty())
    }

    @Test
    fun `getTvSeriesVideos should handle empty results`() = runTest {
        val videosDto = MediaVideosDto(id = 200, results = emptyList())

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesVideos(any(), any()) } returns videosDto

        val result = repository.getTvSeriesVideos(tvSeriesId = 200, language = "en")

        assertTrue(result.results.isEmpty())
    }

    @Test
    fun `getMovieVideos should handle multiple videos`() = runTest {
        val video1 = VideoDto(
            id = "video1",
            key = "key1",
            name = "Trailer",
            site = "YouTube",
            size = 1080,
            type = "Trailer",
            official = true,
            publishedAt = "2023-01-01T00:00:00Z",
            languageCode = "en",
            countryCode = "US"
        )
        val video2 = VideoDto(
            id = "video2",
            key = "key2",
            name = "Teaser",
            site = "YouTube",
            size = 720,
            type = "Teaser",
            official = true,
            publishedAt = "2023-01-02T00:00:00Z",
            languageCode = "en",
            countryCode = "US"
        )
        val videosDto = MediaVideosDto(id = 100, results = listOf(video1, video2))

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieVideos(any(), any()) } returns videosDto

        val result = repository.getMovieVideos(movieId = 100, language = "en")

        assertEquals(2, result.results.size)
        assertEquals("video1", result.results[0].id)
        assertEquals("video2", result.results[1].id)
    }

    @Test
    fun `getTvSeriesVideos should handle multiple videos`() = runTest {
        val video1 = VideoDto(
            id = "video3",
            key = "key3",
            name = "Season Trailer",
            site = "YouTube",
            size = 1080,
            type = "Trailer",
            official = true,
            publishedAt = "2023-03-01T00:00:00Z",
            languageCode = "en",
            countryCode = "US"
        )
        val video2 = VideoDto(
            id = "video4",
            key = "key4",
            name = "Behind the Scenes",
            site = "YouTube",
            size = 720,
            type = "Behind the Scenes",
            official = false,
            publishedAt = "2023-03-02T00:00:00Z",
            languageCode = "en",
            countryCode = "US"
        )
        val videosDto = MediaVideosDto(id = 200, results = listOf(video1, video2))

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesVideos(any(), any()) } returns videosDto

        val result = repository.getTvSeriesVideos(tvSeriesId = 200, language = "en")

        assertEquals(2, result.results.size)
        assertEquals("video3", result.results[0].id)
        assertEquals("video4", result.results[1].id)
    }
}