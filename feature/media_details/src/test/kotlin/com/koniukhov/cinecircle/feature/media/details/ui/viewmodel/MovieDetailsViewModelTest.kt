package com.koniukhov.cinecircle.feature.media.details.ui.viewmodel

import app.cash.turbine.test
import com.koniukhov.cinecircle.core.database.dao.RatedMediaDao
import com.koniukhov.cinecircle.core.database.entity.MediaListEntity
import com.koniukhov.cinecircle.core.database.entity.RatedMediaEntity
import com.koniukhov.cinecircle.core.database.model.MediaListWithCount
import com.koniukhov.cinecircle.core.database.repository.MediaListRepository
import com.koniukhov.cinecircle.core.domain.model.*
import com.koniukhov.cinecircle.core.domain.usecase.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var getCollectionDetailsUseCase: GetCollectionDetailsUseCase
    private lateinit var getMovieImagesUseCase: GetMovieImagesUseCase
    private lateinit var getMovieVideosUseCase: GetMovieVideosUseCase
    private lateinit var getMovieReviewsUseCase: GetMovieReviewsUseCase
    private lateinit var getMovieCreditsUseCase: GetMovieCreditsUseCase
    private lateinit var getMovieRecommendationsUseCase: GetMovieRecommendationsUseCase
    private lateinit var getSimilarMoviesUseCase: GetSimilarMoviesUseCase
    private lateinit var getMovieReleaseDatesUseCase: GetMovieReleaseDatesUseCase
    private lateinit var mediaListRepository: MediaListRepository
    private lateinit var ratedMediaDao: RatedMediaDao
    private lateinit var viewModel: MovieDetailsViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val languageCode = "en"
    private val countryCode = "US"
    private val movieId = 550

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getMovieDetailsUseCase = mockk()
        getCollectionDetailsUseCase = mockk()
        getMovieImagesUseCase = mockk()
        getMovieVideosUseCase = mockk()
        getMovieReviewsUseCase = mockk()
        getMovieCreditsUseCase = mockk()
        getMovieRecommendationsUseCase = mockk()
        getSimilarMoviesUseCase = mockk()
        getMovieReleaseDatesUseCase = mockk()
        mediaListRepository = mockk(relaxed = true)
        ratedMediaDao = mockk(relaxed = true)

        viewModel = MovieDetailsViewModel(
            getMovieDetailsUseCase,
            getCollectionDetailsUseCase,
            getMovieImagesUseCase,
            getMovieVideosUseCase,
            getMovieReviewsUseCase,
            getMovieCreditsUseCase,
            getMovieRecommendationsUseCase,
            getSimilarMoviesUseCase,
            getMovieReleaseDatesUseCase,
            mediaListRepository,
            languageCode,
            countryCode,
            ratedMediaDao
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is not loading with no data`() = runTest(testDispatcher) {
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertNull(state.movieDetails)
        assertNull(state.error)
    }

    @Test
    fun `initMovie sets movie id and loads user rating`() = runTest(testDispatcher) {
        coEvery { ratedMediaDao.getRatedMedia(movieId.toLong()) } returns RatedMediaEntity(movieId.toLong(), 8.5f)

        viewModel.initMovie(movieId)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.userRating.test {
            val rating = awaitItem()
            assertEquals(8.5f, rating, 0.01f)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadMovieDetails updates state with all data on success`() = runTest(testDispatcher) {
        val testMovieDetails = createTestMovieDetails()
        val testCollectionDetails = createTestCollectionDetails()
        val testImages = createTestImages()
        val testVideos = createTestVideos()
        val testReviews = createTestReviews()
        val testCredits = createTestCredits()
        val testRecommendations = createTestRecommendations()
        val testSimilarMovies = createTestSimilarMovies()
        val testReleaseDates = createTestReleaseDates()

        coEvery { getMovieDetailsUseCase(movieId, languageCode) } returns testMovieDetails
        coEvery { getCollectionDetailsUseCase(any(), languageCode) } returns testCollectionDetails
        coEvery { getMovieImagesUseCase(movieId, languageCode) } returns testImages
        coEvery { getMovieVideosUseCase(movieId, languageCode) } returns testVideos
        coEvery { getMovieReviewsUseCase(movieId, 1, languageCode) } returns testReviews
        coEvery { getMovieCreditsUseCase(movieId, languageCode) } returns testCredits
        coEvery { getMovieRecommendationsUseCase(movieId, 1, languageCode) } returns testRecommendations
        coEvery { getSimilarMoviesUseCase(movieId, 1, languageCode) } returns testSimilarMovies
        coEvery { getMovieReleaseDatesUseCase(movieId) } returns testReleaseDates

        viewModel.setMovieId(movieId)
        viewModel.loadMovieDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.movieDetails)
        assertNotNull(state.collectionDetails)
        assertNotNull(state.images)
        assertNotNull(state.videos)
        assertTrue(state.reviews.isNotEmpty())
        assertNotNull(state.credits)
        assertTrue(state.recommendations.isNotEmpty())
        assertTrue(state.similarMovies.isNotEmpty())
        assertTrue(state.releaseDates.isNotEmpty())
        assertNull(state.error)
    }

    @Test
    fun `loadMovieDetails updates state with error on exception`() = runTest(testDispatcher) {
        val errorMessage = "Network error"
        coEvery { getMovieDetailsUseCase(movieId, languageCode) } throws Exception(errorMessage)

        viewModel.setMovieId(movieId)
        viewModel.loadMovieDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        assertNull(state.movieDetails)
    }

    @Test
    fun `checkAndLoadCollections loads collections and checks if movie is in collection`() = runTest(testDispatcher) {
        val testCollections = listOf(
            MediaListEntity(id = 1, name = "Favorites", isDefault = false)
        )

        coEvery { mediaListRepository.getListsContainingMovie(movieId) } returns testCollections
        coEvery { mediaListRepository.getAllLists() } returns flowOf(testCollections)
        coEvery { mediaListRepository.getMediaCountInList(1) } returns 5

        viewModel.setMovieId(movieId)
        viewModel.checkAndLoadCollections()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.isMovieInCollection.test {
            skipItems(1)
            val isInCollection = awaitItem()
            assertTrue(isInCollection)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.allCollections.test {
            val collections = awaitItem()
            assertEquals(1, collections.size)
            assertEquals("Favorites", collections[0].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addMovieToCollection adds movie and returns collection name`() = runTest(testDispatcher) {
        val collectionId = 1L
        val collectionName = "Favorites"
        val collection = MediaListEntity(id = collectionId, name = collectionName, isDefault = false)

        coEvery { mediaListRepository.getListById(collectionId) } returns collection
        coEvery { mediaListRepository.addMovieToList(collectionId, movieId) } returns true
        coEvery { mediaListRepository.getListsContainingMovie(movieId) } returns listOf(collection)

        viewModel.setMovieId(movieId)
        val result = viewModel.addMovieToCollection(collectionId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(collectionName, result)
        coVerify { mediaListRepository.addMovieToList(collectionId, movieId) }
    }

    @Test
    fun `removeMovieFromCollection removes movie from collection`() = runTest(testDispatcher) {
        val collectionId = 1L
        val collection = MediaListEntity(id = collectionId, name = "Favorites", isDefault = true)

        coEvery { mediaListRepository.getListsContainingMovie(movieId) } returns listOf(collection) andThen emptyList()
        coEvery { mediaListRepository.removeMovieFromList(collectionId, movieId) } just Runs

        viewModel.setMovieId(movieId)
        viewModel.checkAndLoadCollections()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.removeMovieFromCollection()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { mediaListRepository.removeMovieFromList(collectionId, movieId) }
    }

    @Test
    fun `setUserRating saves rating to database`() = runTest(testDispatcher) {
        val rating = 4.0f
        coEvery { ratedMediaDao.insertRatedMedia(any()) } just Runs

        viewModel.setMovieId(movieId)
        viewModel.setUserRating(rating)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.userRating.test {
            val savedRating = awaitItem()
            assertEquals(rating, savedRating, 0.01f)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { ratedMediaDao.insertRatedMedia(RatedMediaEntity(movieId.toLong(), rating)) }
    }

    @Test
    fun `loadUserRating loads rating from database`() = runTest(testDispatcher) {
        val rating = 3.5f
        coEvery { ratedMediaDao.getRatedMedia(movieId.toLong()) } returns RatedMediaEntity(movieId.toLong(), rating)

        viewModel.setMovieId(movieId)
        viewModel.loadUserRating()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.userRating.test {
            val loadedRating = awaitItem()
            assertEquals(rating, loadedRating, 0.01f)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadUserRating returns zero when no rating exists`() = runTest(testDispatcher) {
        coEvery { ratedMediaDao.getRatedMedia(movieId.toLong()) } returns null

        viewModel.setMovieId(movieId)
        viewModel.loadUserRating()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.userRating.test {
            val rating = awaitItem()
            assertEquals(0f, rating, 0.01f)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createTestMovieDetails() = MovieDetails(
        id = movieId,
        title = "Fight Club",
        originalTitle = "Fight Club",
        overview = "A ticking-time-bomb insomniac...",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        releaseDate = "1999-10-15",
        runtime = 139,
        voteAverage = 8.4f,
        voteCount = 26000,
        budget = 63000000,
        revenue = 100853753,
        tagline = "Mischief. Mayhem. Soap.",
        adult = false,
        originalLanguage = "en",
        popularity = 61.416f,
        status = "Released",
        genres = listOf(Genre(18, "Drama")),
        belongsToCollection = MovieCollection.empty(),
        productionCompanies = emptyList(),
        productionCountries = emptyList(),
        spokenLanguages = emptyList(),
        homePage = "",
        imdbId = "",
        video = false
    )

    private fun createTestCollectionDetails() = CollectionDetails(
        id = 1,
        name = "Collection",
        overview = "Collection overview",
        posterPath = "/collection_poster.jpg",
        backdropPath = "/collection_backdrop.jpg",
        parts = emptyList()
    )

    private fun createTestImages() = MediaImages(
        id = movieId,
        posters = listOf(Image(filePath = "/poster.jpg", voteAverage = 3.5f, voteCount = 10, width = 500, height = 750, aspectRatio = 0.667f, countryCode = "US")),
        backdrops = listOf(Image(filePath = "/backdrop.jpg", voteAverage = 4.0f, voteCount = 20, width = 1920, height = 1080, aspectRatio = 1.778f, countryCode = "US")),
        logos = emptyList()
    )

    private fun createTestVideos() = MediaVideos(
        id = movieId,
        results = listOf(
            Video(id = "video1", key = "key1", name = "Trailer", site = "YouTube", type = "Trailer", official = true, publishedAt = "2020-01-01", languageCode = "en", countryCode = "US", size = 1080)
        )
    )

    private fun createTestReviews() = listOf(
        MediaReview(
            author = "John Doe",
            authorDetails = ReviewAuthor(name = "John", username = "johndoe", avatarPath = "/avatar.jpg", rating = "9"),
            content = "Great movie!",
            createdAt = "2020-01-01",
            id = "review1",
            updatedAt = "2020-01-01",
            url = "https://example.com"
        )
    )

    private fun createTestCredits() = MediaCredits(
        cast = listOf(
            CastMember(id = 1, name = "Brad Pitt", character = "Tyler Durden", profilePath = "/brad.jpg", order = 0, castId = 1, creditId = "credit1", gender = 2, knownForDepartment = "Acting", adult = false, originalName = "Brad Pitt", popularity = 10f)
        ),
        crew = listOf(
            CrewMember(id = 2, name = "David Fincher", job = "Director", department = "Directing", profilePath = "/david.jpg", creditId = "credit2", gender = 2, knownForDepartment = "Directing", adult = false, originalName = "David Fincher", popularity = 9f)
        )
    )

    private fun createTestRecommendations() = listOf(
        Movie(
            id = 1,
            title = "Recommended Movie",
            posterPath = "/rec_poster.jpg",
            backdropPath = "/rec_backdrop.jpg",
            overview = "Recommended overview",
            releaseDate = "2020-01-01",
            voteAverage = 4.0f,
            voteCount = 1000,
            genreIds = listOf(18),
            adult = false,
            originalLanguage = "en",
            popularity = 50.0f,
            originalTitle = "Recommended Movie",
            video = false
        )
    )

    private fun createTestSimilarMovies() = listOf(
        Movie(
            id = 2,
            title = "Similar Movie",
            posterPath = "/sim_poster.jpg",
            backdropPath = "/sim_backdrop.jpg",
            overview = "Similar overview",
            releaseDate = "2019-01-01",
            voteAverage = 4.5f,
            voteCount = 800,
            genreIds = listOf(18),
            adult = false,
            originalLanguage = "en",
            popularity = 45.0f,
            originalTitle = "Similar Movie",
            video = false
        )
    )

    private fun createTestReleaseDates() = listOf(
        ReleaseDateResult(
            countryCode = "US",
            releaseDates = listOf(
                ReleaseDate(
                    certification = "R",
                    descriptors = emptyList(),
                    languageCode = "en",
                    note = "",
                    releaseDate = "1999-10-15",
                    releaseType = 3
                )
            )
        )
    )
}