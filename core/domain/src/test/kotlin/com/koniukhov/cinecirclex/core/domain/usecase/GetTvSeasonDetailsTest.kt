package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.TvEpisodeDetails
import com.koniukhov.cinecirclex.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecirclex.core.domain.repository.TvSeriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetTvSeasonDetailsTest {

    private lateinit var repository: TvSeriesRepository
    private lateinit var useCase: GetTvSeasonDetails

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetTvSeasonDetails(repository)
    }

    @Test
    fun `invoke returns tv season details from repository`() = runTest {
        val tvSeriesId = 123
        val seasonNumber = 1
        val languageCode = "en"
        val mockSeasonDetails = TvSeasonDetails(
            _id = "season_id_1",
            id = 456,
            airDate = "2023-01-15",
            episodes = listOf(
                TvEpisodeDetails(
                    airDate = "2023-01-15",
                    crew = emptyList(),
                    episodeNumber = 1,
                    guestStars = emptyList(),
                    name = "Episode 1",
                    overview = "First episode",
                    id = 1,
                    productionCode = "",
                    runtime = 45,
                    seasonNumber = 1,
                    stillPath = "/still1.jpg",
                    voteAverage = 8.5f,
                    voteCount = 100
                ),
                TvEpisodeDetails(
                    airDate = "2023-01-22",
                    crew = emptyList(),
                    episodeNumber = 2,
                    guestStars = emptyList(),
                    name = "Episode 2",
                    overview = "Second episode",
                    id = 2,
                    productionCode = "",
                    runtime = 45,
                    seasonNumber = 1,
                    stillPath = "/still2.jpg",
                    voteAverage = 8.8f,
                    voteCount = 120
                )
            ),
            name = "Season 1",
            overview = "First season overview",
            posterPath = "/season1.jpg",
            seasonNumber = 1,
            voteAverage = 8.6f
        )

        coEvery {
            repository.getTvSeasonDetails(tvSeriesId, seasonNumber, languageCode)
        } returns mockSeasonDetails

        val result = useCase(tvSeriesId, seasonNumber, languageCode)

        assertEquals(mockSeasonDetails, result)
        coVerify(exactly = 1) {
            repository.getTvSeasonDetails(tvSeriesId, seasonNumber, languageCode)
        }
    }

    @Test
    fun `invoke returns season details with empty episodes from repository`() = runTest {
        val tvSeriesId = 123
        val seasonNumber = 1
        val languageCode = "en"
        val emptySeasonDetails = TvSeasonDetails(
            _id = "season_id_2",
            id = 456,
            airDate = "2023-01-15",
            episodes = emptyList(),
            name = "Season 1",
            overview = "Season with no episodes",
            posterPath = "/season1.jpg",
            seasonNumber = 1,
            voteAverage = 0.0f
        )

        coEvery {
            repository.getTvSeasonDetails(tvSeriesId, seasonNumber, languageCode)
        } returns emptySeasonDetails

        val result = useCase(tvSeriesId, seasonNumber, languageCode)

        assertEquals(emptySeasonDetails, result)
        coVerify(exactly = 1) {
            repository.getTvSeasonDetails(tvSeriesId, seasonNumber, languageCode)
        }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val tvSeriesId = 123
        val seasonNumber = 1
        val languageCode = "en"

        coEvery {
            repository.getTvSeasonDetails(tvSeriesId, seasonNumber, languageCode)
        } throws Exception("Network error")

        useCase(tvSeriesId, seasonNumber, languageCode)
    }
}