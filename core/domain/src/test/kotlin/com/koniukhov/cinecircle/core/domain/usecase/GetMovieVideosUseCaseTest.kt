package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.MediaVideos
import com.koniukhov.cinecircle.core.domain.model.Video
import com.koniukhov.cinecircle.core.domain.repository.VideosRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMovieVideosUseCaseTest {

    private lateinit var repository: VideosRepository
    private lateinit var useCase: GetMovieVideosUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMovieVideosUseCase(repository)
    }

    @Test
    fun `invoke returns movie videos from repository`() = runTest {
        val movieId = 278
        val language = "en"
        val videos = listOf(
            createTestVideo(id = "1", name = "Official Trailer", type = "Trailer"),
            createTestVideo(id = "2", name = "Behind the Scenes", type = "Featurette")
        )
        val expectedMediaVideos = MediaVideos(id = movieId, results = videos)

        coEvery { repository.getMovieVideos(movieId, language) } returns expectedMediaVideos

        val result = useCase(movieId, language)

        assertEquals(expectedMediaVideos, result)
        assertEquals(movieId, result.id)
        assertEquals(2, result.results.size)
        assertEquals("Official Trailer", result.results[0].name)
        coVerify(exactly = 1) { repository.getMovieVideos(movieId, language) }
    }

    @Test
    fun `invoke returns empty video list when no videos available`() = runTest {
        val movieId = 278
        val language = "en"
        val emptyMediaVideos = MediaVideos(id = movieId, results = emptyList())

        coEvery { repository.getMovieVideos(movieId, language) } returns emptyMediaVideos

        val result = useCase(movieId, language)

        assertTrue(result.results.isEmpty())
        assertEquals(movieId, result.id)
        coVerify(exactly = 1) { repository.getMovieVideos(movieId, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val movieId = 278
        val language = "en"

        coEvery { repository.getMovieVideos(movieId, language) } throws Exception("Network error")

        useCase(movieId, language)
    }

    @Test
    fun `invoke with different movie IDs calls repository correctly`() = runTest {
        val movieId1 = 278
        val movieId2 = 238
        val language = "en"
        val mediaVideos1 = MediaVideos(id = movieId1, results = emptyList())
        val mediaVideos2 = MediaVideos(id = movieId2, results = emptyList())

        coEvery { repository.getMovieVideos(movieId1, any()) } returns mediaVideos1
        coEvery { repository.getMovieVideos(movieId2, any()) } returns mediaVideos2

        useCase(movieId1, language)
        useCase(movieId2, language)

        coVerify(exactly = 1) { repository.getMovieVideos(movieId1, language) }
        coVerify(exactly = 1) { repository.getMovieVideos(movieId2, language) }
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val movieId = 278
        val languageEn = "en"
        val languageEs = "es"
        val mediaVideos = MediaVideos(id = movieId, results = emptyList())

        coEvery { repository.getMovieVideos(any(), any()) } returns mediaVideos

        useCase(movieId, languageEn)
        useCase(movieId, languageEs)

        coVerify(exactly = 1) { repository.getMovieVideos(movieId, languageEn) }
        coVerify(exactly = 1) { repository.getMovieVideos(movieId, languageEs) }
    }

    @Test
    fun `invoke returns only trailer videos`() = runTest {
        val movieId = 278
        val language = "en"
        val videos = listOf(
            createTestVideo(id = "1", type = "Trailer"),
            createTestVideo(id = "2", type = "Trailer"),
            createTestVideo(id = "3", type = "Trailer")
        )
        val mediaVideos = MediaVideos(id = movieId, results = videos)

        coEvery { repository.getMovieVideos(movieId, language) } returns mediaVideos

        val result = useCase(movieId, language)

        assertEquals(3, result.results.size)
        assertTrue(result.results.all { it.type == "Trailer" })
    }

    @Test
    fun `invoke returns videos of different types`() = runTest {
        val movieId = 278
        val language = "en"
        val videos = listOf(
            createTestVideo(id = "1", type = "Trailer"),
            createTestVideo(id = "2", type = "Teaser"),
            createTestVideo(id = "3", type = "Featurette"),
            createTestVideo(id = "4", type = "Behind the Scenes"),
            createTestVideo(id = "5", type = "Clip")
        )
        val mediaVideos = MediaVideos(id = movieId, results = videos)

        coEvery { repository.getMovieVideos(movieId, language) } returns mediaVideos

        val result = useCase(movieId, language)

        assertEquals(5, result.results.size)
        val types = result.results.map { it.type }.toSet()
        assertTrue(types.contains("Trailer"))
        assertTrue(types.contains("Teaser"))
        assertTrue(types.contains("Featurette"))
    }

    @Test
    fun `invoke returns YouTube videos`() = runTest {
        val movieId = 278
        val language = "en"
        val videos = listOf(
            createTestVideo(id = "1", site = "YouTube", key = "abc123"),
            createTestVideo(id = "2", site = "YouTube", key = "def456"),
            createTestVideo(id = "3", site = "YouTube", key = "ghi789")
        )
        val mediaVideos = MediaVideos(id = movieId, results = videos)

        coEvery { repository.getMovieVideos(movieId, language) } returns mediaVideos

        val result = useCase(movieId, language)

        assertEquals(3, result.results.size)
        assertTrue(result.results.all { it.site == "YouTube" })
        assertTrue(result.results.all { it.key.isNotEmpty() })
    }

    @Test
    fun `getYouTubeTrailersAndTeasers returns only YouTube trailers and teasers`() = runTest {
        val movieId = 278
        val language = "en"
        val videos = listOf(
            createTestVideo(id = "1", type = "Trailer", site = "YouTube"),
            createTestVideo(id = "2", type = "Teaser", site = "YouTube"),
            createTestVideo(id = "3", type = "Clip", site = "YouTube"),
            createTestVideo(id = "4", type = "Trailer", site = "Vimeo")
        )
        val mediaVideos = MediaVideos(id = movieId, results = videos)

        coEvery { repository.getMovieVideos(movieId, language) } returns mediaVideos

        val result = useCase(movieId, language)
        val filteredVideos = result.getYouTubeTrailersAndTeasers()

        assertEquals(2, filteredVideos.size)
        assertTrue(filteredVideos.all { it.site == "YouTube" })
        assertTrue(filteredVideos.all { it.type == "Trailer" || it.type == "Teaser" })
    }

    private fun createTestVideo(
        id: String = "1",
        name: String = "Test Video",
        type: String = "Trailer",
        key: String = "abc123",
        site: String = "YouTube",
        size: Int = 1080,
        official: Boolean = true
    ) = Video(
        languageCode = "en",
        countryCode = "US",
        name = name,
        key = key,
        site = site,
        size = size,
        type = type,
        official = official,
        publishedAt = "2024-01-01T00:00:00.000Z",
        id = id
    )
}