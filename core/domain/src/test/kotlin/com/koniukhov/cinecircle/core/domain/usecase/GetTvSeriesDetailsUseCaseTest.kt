package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.TvSeriesDetails
import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTvSeriesDetailsUseCaseTest {

    private lateinit var repository: TvSeriesRepository
    private lateinit var useCase: GetTvSeriesDetailsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetTvSeriesDetailsUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke returns tv series details from repository`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"
        val expectedDetails = TvSeriesDetails.empty().copy(
            id = 1396,
            name = "Breaking Bad",
            originalName = "Breaking Bad",
            overview = "A high school chemistry teacher turned methamphetamine producer",
            posterPath = "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
            backdropPath = "/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg",
            firstAirDate = "2008-01-20",
            lastAirDate = "2013-09-29",
            voteAverage = 9.3f,
            voteCount = 13000,
            popularity = 200.0f,
            numberOfSeasons = 5,
            numberOfEpisodes = 62,
            status = "Ended",
            type = "Scripted",
            inProduction = false,
            homepage = "http://www.amc.com/shows/breaking-bad"
        )

        coEvery { repository.getTvSeriesDetails(tvSeriesId, languageCode) } returns expectedDetails

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedDetails, result)
        assertEquals(1396, result.id)
        assertEquals("Breaking Bad", result.name)
        coVerify(exactly = 1) { repository.getTvSeriesDetails(tvSeriesId, languageCode) }
    }

    @Test
    fun `invoke returns empty details when no data available`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"
        val expectedDetails = TvSeriesDetails.empty()

        coEvery { repository.getTvSeriesDetails(tvSeriesId, languageCode) } returns expectedDetails

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedDetails, result)
        assertEquals("", result.name)
        assertEquals(0, result.numberOfSeasons)
        coVerify(exactly = 1) { repository.getTvSeriesDetails(tvSeriesId, languageCode) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"

        coEvery { repository.getTvSeriesDetails(tvSeriesId, languageCode) } throws Exception("Network error")

        useCase(tvSeriesId, languageCode)
    }

    @Test
    fun `invoke handles different tv series ids correctly`() = runTest {
        val tvSeriesId = 1668
        val languageCode = "en"
        val expectedDetails = TvSeriesDetails.empty().copy(
            id = 1668,
            name = "Friends",
            originalName = "Friends",
            overview = "Six friends navigate life and love in New York City",
            voteAverage = 8.4f,
            voteCount = 5000,
            popularity = 150.0f,
            numberOfSeasons = 10,
            numberOfEpisodes = 236
        )

        coEvery { repository.getTvSeriesDetails(tvSeriesId, languageCode) } returns expectedDetails

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedDetails, result)
        assertEquals("Friends", result.name)
        coVerify(exactly = 1) { repository.getTvSeriesDetails(tvSeriesId, languageCode) }
    }

    @Test
    fun `invoke returns details with multiple seasons`() = runTest {
        val tvSeriesId = 1399
        val languageCode = "en"
        val expectedDetails = TvSeriesDetails.empty().copy(
            id = 1399,
            name = "Game of Thrones",
            originalName = "Game of Thrones",
            overview = "Epic fantasy series",
            voteAverage = 8.4f,
            voteCount = 20000,
            popularity = 300.0f,
            numberOfSeasons = 8,
            numberOfEpisodes = 73,
            status = "Ended"
        )

        coEvery { repository.getTvSeriesDetails(tvSeriesId, languageCode) } returns expectedDetails

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedDetails, result)
        assertEquals(8, result.numberOfSeasons)
        assertEquals(73, result.numberOfEpisodes)
        coVerify(exactly = 1) { repository.getTvSeriesDetails(tvSeriesId, languageCode) }
    }

    @Test
    fun `invoke returns details for currently airing series`() = runTest {
        val tvSeriesId = 2316
        val languageCode = "en"
        val expectedDetails = TvSeriesDetails.empty().copy(
            id = 2316,
            name = "The Office",
            originalName = "The Office",
            overview = "Mockumentary about office employees",
            voteAverage = 8.5f,
            voteCount = 3500,
            popularity = 500.0f,
            numberOfSeasons = 9,
            numberOfEpisodes = 201,
            status = "Ended",
            inProduction = false
        )

        coEvery { repository.getTvSeriesDetails(tvSeriesId, languageCode) } returns expectedDetails

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedDetails, result)
        assertEquals("Ended", result.status)
        assertEquals(false, result.inProduction)
        coVerify(exactly = 1) { repository.getTvSeriesDetails(tvSeriesId, languageCode) }
    }
}