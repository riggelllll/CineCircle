package com.koniukhov.cinecirclex.feature.media.details.ui.viewmodel

import app.cash.turbine.test
import com.koniukhov.cinecirclex.core.database.dao.RatedMediaDao
import com.koniukhov.cinecirclex.core.database.entity.MediaListEntity
import com.koniukhov.cinecirclex.core.database.entity.RatedMediaEntity
import com.koniukhov.cinecirclex.core.database.repository.MediaListRepository
import com.koniukhov.cinecirclex.core.domain.model.ContentRating
import com.koniukhov.cinecirclex.core.domain.model.Genre
import com.koniukhov.cinecirclex.core.domain.model.MediaCredits
import com.koniukhov.cinecirclex.core.domain.model.MediaImages
import com.koniukhov.cinecirclex.core.domain.model.MediaReview
import com.koniukhov.cinecirclex.core.domain.model.MediaVideos
import com.koniukhov.cinecirclex.core.domain.model.TvEpisodeDetails
import com.koniukhov.cinecirclex.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecirclex.core.domain.model.TvSeries
import com.koniukhov.cinecirclex.core.domain.model.TvSeriesDetails
import com.koniukhov.cinecirclex.core.domain.usecase.GetSimilarTvSeriesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeasonDetails
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesContentRatingsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesCreditsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesDetailsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesImagesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesRecommendationUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesReviewsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesVideosUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TvSeriesDetailsViewModelTest {

    private lateinit var getTvSeriesDetailsUseCase: GetTvSeriesDetailsUseCase
    private lateinit var getTvSeasonDetails: GetTvSeasonDetails
    private lateinit var getTvSeriesImagesUseCase: GetTvSeriesImagesUseCase
    private lateinit var getTvSeriesVideosUseCase: GetTvSeriesVideosUseCase
    private lateinit var getTvSeriesReviewsUseCase: GetTvSeriesReviewsUseCase
    private lateinit var getTvSeriesCreditsUseCase: GetTvSeriesCreditsUseCase
    private lateinit var getTvSeriesContentRatingsUseCase: GetTvSeriesContentRatingsUseCase
    private lateinit var getTvSeriesRecommendationUseCase: GetTvSeriesRecommendationUseCase
    private lateinit var getSimilarTvSeriesUseCase: GetSimilarTvSeriesUseCase
    private lateinit var mediaListRepository: MediaListRepository
    private lateinit var ratedMediaDao: RatedMediaDao
    private lateinit var viewModel: TvSeriesDetailsViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val languageCode = "en"
    private val countryCode = "US"
    private val tvSeriesId = 1396

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getTvSeriesDetailsUseCase = mockk()
        getTvSeasonDetails = mockk()
        getTvSeriesImagesUseCase = mockk()
        getTvSeriesVideosUseCase = mockk()
        getTvSeriesReviewsUseCase = mockk()
        getTvSeriesCreditsUseCase = mockk()
        getTvSeriesContentRatingsUseCase = mockk()
        getTvSeriesRecommendationUseCase = mockk()
        getSimilarTvSeriesUseCase = mockk()
        mediaListRepository = mockk(relaxed = true)
        ratedMediaDao = mockk(relaxed = true)

        viewModel = TvSeriesDetailsViewModel(
            getTvSeriesDetailsUseCase = getTvSeriesDetailsUseCase,
            getTvSeasonDetails = getTvSeasonDetails,
            getTvSeriesImagesUseCase = getTvSeriesImagesUseCase,
            getTvSeriesVideosUseCase = getTvSeriesVideosUseCase,
            getTvSeriesReviewsUseCase = getTvSeriesReviewsUseCase,
            getTvSeriesCreditsUseCase = getTvSeriesCreditsUseCase,
            getTvSeriesContentRatingsUseCase = getTvSeriesContentRatingsUseCase,
            getTvSeriesRecommendationUseCase = getTvSeriesRecommendationUseCase,
            getSimilarTvSeriesUseCase = getSimilarTvSeriesUseCase,
            mediaListRepository = mediaListRepository,
            languageCode = languageCode,
            countryCode = countryCode,
            ratedMediaDao = ratedMediaDao
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading with no data`() = runTest(testDispatcher) {
        val state = viewModel.uiState.value

        assertTrue(state.isLoading)
        assertNull(state.details)
        assertNull(state.error)
    }

    @Test
    fun `initTvSeries sets tv series id and loads user rating`() = runTest(testDispatcher) {
        coEvery { ratedMediaDao.getRatedMedia(tvSeriesId.toLong()) } returns RatedMediaEntity(tvSeriesId.toLong(), 9.0f)

        viewModel.initTvSeries(tvSeriesId)
        advanceUntilIdle()

        viewModel.userRating.test {
            val rating = awaitItem()
            assertEquals(9.0f, rating, 0.01f)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { ratedMediaDao.getRatedMedia(tvSeriesId.toLong()) }
    }

    @Test
    fun `loadTvSeriesDetails should load all data successfully`() = runTest(testDispatcher) {
        val details = createTestTvSeriesDetails()
        val season = createTestSeasonDetails()
        val images = MediaImages(
            id = tvSeriesId,
            backdrops = emptyList(),
            posters = emptyList(),
            logos = emptyList()
        )
        val videos = MediaVideos(id = tvSeriesId, results = emptyList())
        val reviews = emptyList<MediaReview>()
        val credits = MediaCredits(cast = emptyList(), crew = emptyList())
        val contentRatings = emptyList<ContentRating>()
        val recommendations = emptyList<TvSeries>()
        val similar = emptyList<TvSeries>()

        coEvery { getTvSeriesDetailsUseCase(tvSeriesId, languageCode) } returns details
        coEvery { getTvSeasonDetails(tvSeriesId, any(), languageCode) } returns season
        coEvery { getTvSeriesImagesUseCase(tvSeriesId, languageCode) } returns images
        coEvery { getTvSeriesVideosUseCase(tvSeriesId, languageCode) } returns videos
        coEvery { getTvSeriesReviewsUseCase(tvSeriesId, languageCode, 1) } returns reviews
        coEvery { getTvSeriesCreditsUseCase(tvSeriesId, languageCode) } returns credits
        coEvery { getTvSeriesContentRatingsUseCase(tvSeriesId) } returns contentRatings
        coEvery { getTvSeriesRecommendationUseCase(tvSeriesId, languageCode, 1) } returns recommendations
        coEvery { getSimilarTvSeriesUseCase(tvSeriesId, languageCode, 1) } returns similar

        viewModel.setTvSeriesId(tvSeriesId)
        viewModel.loadTvSeriesDetails()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertNotNull(state.details)
        assertEquals("Breaking Bad", state.details?.name)

        coVerify { getTvSeriesDetailsUseCase(tvSeriesId, languageCode) }
        coVerify { getTvSeriesImagesUseCase(tvSeriesId, languageCode) }
        coVerify { getTvSeriesVideosUseCase(tvSeriesId, languageCode) }
    }

    @Test
    fun `loadTvSeriesDetails should handle error`() = runTest(testDispatcher) {
        val errorMessage = "Network error"
        coEvery { getTvSeriesDetailsUseCase(tvSeriesId, languageCode) } throws Exception(errorMessage)

        viewModel.setTvSeriesId(tvSeriesId)
        viewModel.loadTvSeriesDetails()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        assertNull(state.details)

        coVerify { getTvSeriesDetailsUseCase(tvSeriesId, languageCode) }
    }

    @Test
    fun `addTvSeriesToCollection should add tv series and return collection name`() = runTest(testDispatcher) {
        val collectionId = 1L
        val collectionName = "My Favorites"
        val collection = MediaListEntity(id = collectionId, name = collectionName, isDefault = false)

        coEvery { mediaListRepository.getListById(collectionId) } returns collection
        coEvery { mediaListRepository.addTvSeriesToList(collectionId, tvSeriesId) } returns true
        coEvery { mediaListRepository.getListsContainingTvSeries(tvSeriesId) } returns listOf(collection)

        viewModel.setTvSeriesId(tvSeriesId)
        val result = viewModel.addTvSeriesToCollection(collectionId)
        advanceUntilIdle()

        assertEquals(collectionName, result)
        coVerify { mediaListRepository.addTvSeriesToList(collectionId, tvSeriesId) }
        coVerify { mediaListRepository.getListsContainingTvSeries(tvSeriesId) }
    }

    @Test
    fun `removeTvSeriesFromCollection should remove tv series`() = runTest(testDispatcher) {
        val collectionId = 1L
        val collection = MediaListEntity(id = collectionId, name = "Test Collection", isDefault = false)

        coEvery { mediaListRepository.getListsContainingTvSeries(tvSeriesId) } returns listOf(collection) andThen emptyList()
        coEvery { mediaListRepository.removeTvSeriesFromList(collectionId, tvSeriesId) } returns Unit

        viewModel.setTvSeriesId(tvSeriesId)
        viewModel.checkAndLoadCollections()
        advanceUntilIdle()

        viewModel.removeTvSeriesFromCollection()
        advanceUntilIdle()

        coVerify { mediaListRepository.removeTvSeriesFromList(collectionId, tvSeriesId) }
    }

    @Test
    fun `setUserRating should save rating`() = runTest(testDispatcher) {
        val rating = 8.5f
        coEvery { ratedMediaDao.insertRatedMedia(any()) } returns Unit

        viewModel.setTvSeriesId(tvSeriesId)
        viewModel.setUserRating(rating)
        advanceUntilIdle()

        viewModel.userRating.test {
            val savedRating = awaitItem()
            assertEquals(rating, savedRating, 0.01f)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = tvSeriesId.toLong(), rating = rating)) }
    }

    @Test
    fun `loadUserRating should return 0 when no rating exists`() = runTest(testDispatcher) {
        coEvery { ratedMediaDao.getRatedMedia(tvSeriesId.toLong()) } returns null

        viewModel.setTvSeriesId(tvSeriesId)
        viewModel.loadUserRating()
        advanceUntilIdle()

        viewModel.userRating.test {
            val rating = awaitItem()
            assertEquals(0f, rating, 0.01f)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `checkAndLoadCollections should load all collections`() = runTest(testDispatcher) {
        val collections = listOf(
            MediaListEntity(id = 1L, name = "Favorites", isDefault = false),
            MediaListEntity(id = 2L, name = "Watch Later", isDefault = false)
        )

        coEvery { mediaListRepository.getListsContainingTvSeries(tvSeriesId) } returns emptyList()
        coEvery { mediaListRepository.getAllLists() } returns flowOf(collections)
        coEvery { mediaListRepository.getMediaCountInList(any()) } returns 5

        viewModel.setTvSeriesId(tvSeriesId)
        viewModel.checkAndLoadCollections()
        advanceUntilIdle()

        viewModel.allCollections.test {
            val loadedCollections = awaitItem()
            assertEquals(2, loadedCollections.size)
            assertEquals("Favorites", loadedCollections[0].name)
            assertEquals("Watch Later", loadedCollections[1].name)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { mediaListRepository.getAllLists() }
    }

    @Test
    fun `isTvSeriesInCollection should return true when tv series is in collection`() = runTest(testDispatcher) {
        val collection = MediaListEntity(id = 1L, name = "My Collection", isDefault = false)

        coEvery { mediaListRepository.getListsContainingTvSeries(tvSeriesId) } returns listOf(collection)

        viewModel.setTvSeriesId(tvSeriesId)
        viewModel.checkAndLoadCollections()
        advanceUntilIdle()

        viewModel.isTvSeriesInCollection.test {
            skipItems(1)
            val isInCollection = awaitItem()
            assertTrue(isInCollection)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `isTvSeriesInCollection should return false when tv series is not in collection`() = runTest(testDispatcher) {
        coEvery { mediaListRepository.getListsContainingTvSeries(tvSeriesId) } returns emptyList()
        coEvery { mediaListRepository.getAllLists() } returns flowOf(emptyList())

        viewModel.setTvSeriesId(tvSeriesId)
        viewModel.checkAndLoadCollections()
        advanceUntilIdle()

        viewModel.isTvSeriesInCollection.test {
            val isInCollection = awaitItem()
            assertFalse(isInCollection)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createTestTvSeriesDetails() = TvSeriesDetails(
        id = tvSeriesId,
        name = "Breaking Bad",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        overview = "A high school chemistry teacher turned meth cook",
        firstAirDate = "2008-01-20",
        voteAverage = 9.5f,
        voteCount = 10000,
        genres = listOf(Genre(18, "Drama"), Genre(80, "Crime")),
        popularity = 150.0f,
        adult = false,
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalName = "Breaking Bad",
        numberOfSeasons = 5,
        numberOfEpisodes = 62,
        status = "Ended",
        type = "Scripted",
        homepage = "https://www.amc.com/shows/breaking-bad",
        inProduction = false,
        languages = listOf("en"),
        lastAirDate = "2013-09-29",
        networks = emptyList(),
        productionCompanies = emptyList(),
        productionCountries = emptyList(),
        seasons = listOf(createTestSeasonDetails()),
        spokenLanguages = emptyList(),
        tagline = "Change the equation",
        createdBy = emptyList(),
        episodeRunTime = emptyList(),
        lastEpisodeToAir = TvEpisodeDetails.empty(),
        nextEpisodeToAir = null
    )

    private fun createTestSeasonDetails() = TvSeasonDetails(
        _id = "season_1",
        airDate = "2008-01-20",
        episodes = emptyList(),
        name = "Season 1",
        overview = "Season 1 overview",
        id = 1,
        posterPath = "/season1.jpg",
        seasonNumber = 1,
        voteAverage = 8.0f
    )
}