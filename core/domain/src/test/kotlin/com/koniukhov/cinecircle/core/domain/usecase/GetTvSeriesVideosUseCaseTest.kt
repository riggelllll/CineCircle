package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.MediaVideos
import com.koniukhov.cinecircle.core.domain.model.Video
import com.koniukhov.cinecircle.core.domain.repository.VideosRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTvSeriesVideosUseCaseTest {

    private lateinit var repository: VideosRepository
    private lateinit var useCase: GetTvSeriesVideosUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetTvSeriesVideosUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke returns tv series videos from repository`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"
        val expectedVideos = MediaVideos(
            id = 1396,
            results = listOf(
                Video(
                    languageCode = "en",
                    countryCode = "US",
                    name = "Official Trailer",
                    key = "HhesaQXLuRY",
                    site = "YouTube",
                    size = 1080,
                    type = "Trailer",
                    official = true,
                    publishedAt = "2023-01-15T10:00:00.000Z",
                    id = "video1"
                ),
                Video(
                    languageCode = "en",
                    countryCode = "US",
                    name = "Behind the Scenes",
                    key = "xyz123456",
                    site = "YouTube",
                    size = 1080,
                    type = "Featurette",
                    official = true,
                    publishedAt = "2023-01-20T14:30:00.000Z",
                    id = "video2"
                )
            )
        )

        coEvery { repository.getTvSeriesVideos(tvSeriesId, languageCode) } returns expectedVideos

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedVideos, result)
        assertEquals(2, result.results.size)
        coVerify(exactly = 1) { repository.getTvSeriesVideos(tvSeriesId, languageCode) }
    }

    @Test
    fun `invoke returns empty videos when no videos available`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"
        val expectedVideos = MediaVideos.empty()

        coEvery { repository.getTvSeriesVideos(tvSeriesId, languageCode) } returns expectedVideos

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedVideos, result)
        assertEquals(0, result.results.size)
        coVerify(exactly = 1) { repository.getTvSeriesVideos(tvSeriesId, languageCode) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"

        coEvery { repository.getTvSeriesVideos(tvSeriesId, languageCode) } throws Exception("Network error")

        useCase(tvSeriesId, languageCode)
    }

    @Test
    fun `invoke handles different tv series ids correctly`() = runTest {
        val tvSeriesId = 1668
        val languageCode = "en"
        val expectedVideos = MediaVideos(
            id = 1668,
            results = listOf(
                Video(
                    languageCode = "en",
                    countryCode = "US",
                    name = "Friends Trailer",
                    key = "abc123",
                    site = "YouTube",
                    size = 720,
                    type = "Trailer",
                    official = true,
                    publishedAt = "2023-02-10T12:00:00.000Z",
                    id = "video3"
                )
            )
        )

        coEvery { repository.getTvSeriesVideos(tvSeriesId, languageCode) } returns expectedVideos

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedVideos, result)
        coVerify(exactly = 1) { repository.getTvSeriesVideos(tvSeriesId, languageCode) }
    }

    @Test
    fun `invoke handles videos with different types`() = runTest {
        val tvSeriesId = 60059
        val languageCode = "en"
        val expectedVideos = MediaVideos(
            id = 60059,
            results = listOf(
                Video(
                    languageCode = "en",
                    countryCode = "US",
                    name = "Official Trailer",
                    key = "trailer123",
                    site = "YouTube",
                    size = 1080,
                    type = "Trailer",
                    official = true,
                    publishedAt = "2023-01-01T10:00:00.000Z",
                    id = "v1"
                ),
                Video(
                    languageCode = "en",
                    countryCode = "US",
                    name = "Featurette",
                    key = "featurette123",
                    site = "YouTube",
                    size = 720,
                    type = "Featurette",
                    official = true,
                    publishedAt = "2023-01-02T11:00:00.000Z",
                    id = "v2"
                ),
                Video(
                    languageCode = "en",
                    countryCode = "US",
                    name = "Teaser",
                    key = "teaser123",
                    site = "YouTube",
                    size = 1080,
                    type = "Teaser",
                    official = true,
                    publishedAt = "2023-01-03T12:00:00.000Z",
                    id = "v3"
                )
            )
        )

        coEvery { repository.getTvSeriesVideos(tvSeriesId, languageCode) } returns expectedVideos

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedVideos, result)
        assertEquals(3, result.results.size)
        assertEquals("Trailer", result.results[0].type)
        assertEquals("Featurette", result.results[1].type)
        assertEquals("Teaser", result.results[2].type)
        coVerify(exactly = 1) { repository.getTvSeriesVideos(tvSeriesId, languageCode) }
    }

    @Test
    fun `invoke handles unofficial videos`() = runTest {
        val tvSeriesId = 84958
        val languageCode = "en"
        val expectedVideos = MediaVideos(
            id = 84958,
            results = listOf(
                Video(
                    languageCode = "en",
                    countryCode = "US",
                    name = "Fan Made Trailer",
                    key = "fanmade123",
                    site = "YouTube",
                    size = 720,
                    type = "Trailer",
                    official = false,
                    publishedAt = "2023-04-15T18:30:00.000Z",
                    id = "video5"
                )
            )
        )

        coEvery { repository.getTvSeriesVideos(tvSeriesId, languageCode) } returns expectedVideos

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedVideos, result)
        assertEquals(false, result.results[0].official)
        coVerify(exactly = 1) { repository.getTvSeriesVideos(tvSeriesId, languageCode) }
    }
}