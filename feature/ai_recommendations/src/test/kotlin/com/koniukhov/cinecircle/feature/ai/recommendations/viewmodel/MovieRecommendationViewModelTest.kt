package com.koniukhov.cinecircle.feature.ai.recommendations.viewmodel

import app.cash.turbine.test
import com.koniukhov.cinecircle.core.database.dao.RatedMediaDao
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import com.koniukhov.cinecircle.feature.ai.recommendations.MediaRepository
import com.koniukhov.cinecircle.feature.ai.recommendations.MediaVector
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRecommendationViewModelTest {

    private lateinit var viewModel: MovieRecommendationViewModel
    private lateinit var mediaRepository: MediaRepository
    private lateinit var ratedMediaDao: RatedMediaDao
    private lateinit var moviesRepository: MoviesRepository
    private lateinit var tvSeriesRepository: TvSeriesRepository

    private val testDispatcher = StandardTestDispatcher()
    private val languageCode = "en"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mediaRepository = mockk(relaxed = true)
        ratedMediaDao = mockk()
        moviesRepository = mockk()
        tvSeriesRepository = mockk()

        viewModel = MovieRecommendationViewModel(
            repository = mediaRepository,
            ratedMediaDao = ratedMediaDao,
            moviesRepository = moviesRepository,
            tvSeriesRepository = tvSeriesRepository,
            languageCode = languageCode
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `generateRecommendations should calculate and return top recommendations`() = runTest(testDispatcher) {
        val userRatings = mapOf(
            1 to 4.5f,
            2 to 5.0f
        )

        val movieVectors = listOf(
            MediaVector(movieId = 1, tmdbId = 1, vector = floatArrayOf(0.1f, 0.2f, 0.3f)),
            MediaVector(movieId = 2, tmdbId = 2, vector = floatArrayOf(0.2f, 0.3f, 0.4f)),
            MediaVector(movieId = 3, tmdbId = 3, vector = floatArrayOf(0.15f, 0.25f, 0.35f)),
            MediaVector(movieId = 4, tmdbId = 4, vector = floatArrayOf(0.3f, 0.4f, 0.5f))
        )

        coEvery { mediaRepository.initialize() } returns Unit
        coEvery { mediaRepository.getAllMovieVectors() } returns movieVectors

        viewModel.recommendations.test {
            skipItems(1) // skip initial empty state

            viewModel.generateRecommendations(userRatings, topN = 2)
            advanceUntilIdle()

            val recommendations = awaitItem()
            assertEquals(2, recommendations.size)
            assertFalse(recommendations.any { it.tmdbId == 1 })
            assertFalse(recommendations.any { it.tmdbId == 2 })

            cancelAndIgnoreRemainingEvents()
        }

        viewModel.isLoading.test {
            val loading = awaitItem()
            assertFalse(loading)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { mediaRepository.initialize() }
        coVerify { mediaRepository.getAllMovieVectors() }
    }

    @Test
    fun `generateRecommendations should not run when userRatings is empty`() = runTest(testDispatcher) {
        viewModel.generateRecommendations(emptyMap(), topN = 10)
        advanceUntilIdle()

        viewModel.recommendations.test {
            val recommendations = awaitItem()
            assertTrue(recommendations.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `generateRecommendationsFromDatabase should set hasNoRatings when no rated media`() = runTest(testDispatcher) {
        coEvery { ratedMediaDao.getAllRatedMedias() } returns emptyList()

        viewModel.generateRecommendationsFromDatabase(topN = 10)
        advanceUntilIdle()

        viewModel.hasNoRatings.test {
            val hasNoRatings = awaitItem()
            assertTrue(hasNoRatings)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.isLoading.test {
            val loading = awaitItem()
            assertFalse(loading)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.recommendedMedia.test {
            val media = awaitItem()
            assertTrue(media.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { ratedMediaDao.getAllRatedMedias() }
    }
}