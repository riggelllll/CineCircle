package com.koniukhov.cinecirclex.feature.media.details.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koniukhov.cinecirclex.core.common.Constants.ENGLISH_LANGUAGE_CODE
import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID
import com.koniukhov.cinecirclex.core.common.util.getLocalizedLanguageMap
import com.koniukhov.cinecirclex.core.data.di.CountryCode
import com.koniukhov.cinecirclex.core.data.di.LanguageCode
import com.koniukhov.cinecirclex.core.data.mapper.toMovieWithGenres
import com.koniukhov.cinecirclex.core.database.dao.RatedMediaDao
import com.koniukhov.cinecirclex.core.database.entity.RatedMediaEntity
import com.koniukhov.cinecirclex.core.database.entity.MediaListEntity
import com.koniukhov.cinecirclex.core.database.model.MediaListWithCount
import com.koniukhov.cinecirclex.core.database.repository.MediaListRepository
import com.koniukhov.cinecirclex.core.domain.usecase.GetCollectionDetailsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetMovieCreditsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetMovieDetailsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetMovieImagesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetMovieRecommendationsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetMovieReleaseDatesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetMovieReviewsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetMovieVideosUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetSimilarMoviesUseCase
import com.koniukhov.cinecirclex.feature.media.details.ui.state.MovieDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getCollectionDetailsUseCase: GetCollectionDetailsUseCase,
    private val getMovieImagesUseCase: GetMovieImagesUseCase,
    private val getMovieVideosUseCase: GetMovieVideosUseCase,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getMovieCreditsUseCase: GetMovieCreditsUseCase,
    private val getMovieRecommendationsUseCase: GetMovieRecommendationsUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val getMovieReleaseDatesUseCase: GetMovieReleaseDatesUseCase,
    private val mediaListRepository: MediaListRepository,
    @LanguageCode
    private val languageCode: String,
    @CountryCode
    val countryCode: String,
    private val ratedMediaDao: RatedMediaDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()
    private var _movieId = MutableStateFlow(INVALID_ID)

    private val _mediaCollection = MutableStateFlow<MediaListEntity?>(null)

    val isMovieInCollection: StateFlow<Boolean> = _mediaCollection.asStateFlow().map { it != null }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private val _allCollections = MutableStateFlow<List<MediaListWithCount>>(emptyList())
    val allCollections: StateFlow<List<MediaListWithCount>> = _allCollections.asStateFlow()

    private val _userRating = MutableStateFlow(0f)
    val userRating: StateFlow<Float> = _userRating.asStateFlow()

    var languages: Map<String, String> = Locale.getDefault().getLocalizedLanguageMap()
        private set

    fun initMovie(id: Int) {
        setMovieId(id)
        loadUserRating()
    }

    fun setMovieId(id: Int) {
        _movieId.value = id
    }

    fun checkAndLoadCollections(){
        checkMediaCollection()
        loadAllCollections()
    }

    private fun checkMediaCollection() {
        viewModelScope.launch {
            try {
                val collections = mediaListRepository.getListsContainingMovie(_movieId.value)
                _mediaCollection.value = collections.firstOrNull()
            } catch (e: Exception) {
                Timber.e(e)
                _mediaCollection.value = null
            }
        }
    }

    private fun loadAllCollections() {
        viewModelScope.launch {
            mediaListRepository.getAllLists().collect { lists ->
                val collectionsWithCount = lists.map { list ->
                    val itemCount = mediaListRepository.getMediaCountInList(list.id)
                    MediaListWithCount(
                        id = list.id,
                        name = list.name,
                        itemCount = itemCount,
                        isDefault = list.isDefault
                    )
                }
                _allCollections.value = collectionsWithCount
            }
        }
    }

    suspend fun addMovieToCollection(collectionId: Long): String {
        return try {

            val collection = mediaListRepository.getListById(collectionId)
            val collectionName = collection?.name ?: ""

            val success = mediaListRepository.addMovieToList(collectionId, _movieId.value)
            if (success) {
                checkMediaCollection()
            }
            collectionName
        } catch (e: Exception) {
            Timber.e(e)
            ""
        }
    }

    fun removeMovieFromCollection() {
        viewModelScope.launch {
            try {
                _mediaCollection.value?.let { currentCollection ->
                    mediaListRepository.removeMovieFromList(currentCollection.id, _movieId.value)
                    checkMediaCollection()
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun loadMovieDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val movieDetails = getMovieDetailsUseCase(_movieId.value, languageCode)

                val collectionDetailsDeferred = async {
                    getCollectionDetailsUseCase(movieDetails.belongsToCollection.id, languageCode)
                }

                val imagesDeferred = async {
                    var images = getMovieImagesUseCase(movieDetails.id, languageCode)
                    if ((images.posters.isEmpty() && images.backdrops.isEmpty()) && languageCode != ENGLISH_LANGUAGE_CODE) {
                        images = getMovieImagesUseCase(movieDetails.id, ENGLISH_LANGUAGE_CODE)
                    }
                    images
                }

                val videosDeferred = async {
                    var videos = getMovieVideosUseCase(movieDetails.id, languageCode)
                    if (videos.results.isEmpty() && languageCode != ENGLISH_LANGUAGE_CODE) {
                        videos = getMovieVideosUseCase(movieDetails.id, ENGLISH_LANGUAGE_CODE)
                    }
                    videos
                }

                val reviewsDeferred = async {
                    getMovieReviewsUseCase(movieDetails.id, 1, languageCode)
                }

                val creditsDeferred = async {
                    getMovieCreditsUseCase(movieDetails.id, languageCode)
                }

                val recommendationsDeferred = async {
                    getMovieRecommendationsUseCase(movieDetails.id, 1, languageCode)
                }

                val similarMoviesDeferred = async {
                    getSimilarMoviesUseCase(movieDetails.id, 1, languageCode)
                }

                val releaseDatesDeferred = async {
                    getMovieReleaseDatesUseCase(movieDetails.id)
                }

                val collectionDetails = collectionDetailsDeferred.await()
                val images = imagesDeferred.await()
                val videos = videosDeferred.await()
                val reviews = reviewsDeferred.await()
                val credits = creditsDeferred.await()
                val recommendations = recommendationsDeferred.await()
                val similarMovies = similarMoviesDeferred.await()
                val releaseDates = releaseDatesDeferred.await()

                _uiState.value = MovieDetailsUiState(
                    isLoading = false,
                    movieDetails = movieDetails,
                    collectionDetails = collectionDetails,
                    images = images,
                    videos = videos,
                    reviews = reviews,
                    credits = credits,
                    recommendations = recommendations,
                    similarMovies = similarMovies,
                    releaseDates = releaseDates,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun cacheMovieDetails() {
        val movieDetails = _uiState.value.movieDetails
        viewModelScope.launch {
            movieDetails?.let {
                mediaListRepository.insertMovieWithGenres(movieDetails.toMovieWithGenres(_movieId.value))
            }
        }
    }

    fun removeMovieDetailsFromCache() {
        viewModelScope.launch {
            mediaListRepository.deleteMovieWithGenres(_movieId.value)
        }
    }

    fun loadUserRating() {
        val id = _movieId.value
        if (id == INVALID_ID) return
        viewModelScope.launch {
            try {
                val rated = ratedMediaDao.getRatedMedia(id.toLong())
                _userRating.value = rated?.rating ?: 0f
            } catch (e: Exception) {
                Timber.e(e)
                _userRating.value = 0f
            }
        }
    }

    fun setUserRating(rating: Float) {
        val id = _movieId.value
        if (id == INVALID_ID) return
        viewModelScope.launch {
            try {
                ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = id.toLong(), rating = rating))
                _userRating.value = rating
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}