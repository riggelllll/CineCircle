package com.koniukhov.cinecirclex.feature.media.details.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koniukhov.cinecirclex.core.common.Constants.ENGLISH_LANGUAGE_CODE
import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID
import com.koniukhov.cinecirclex.core.common.util.getLocalizedLanguageMap
import com.koniukhov.cinecirclex.core.data.di.CountryCode
import com.koniukhov.cinecirclex.core.data.di.LanguageCode
import com.koniukhov.cinecirclex.core.data.mapper.toTvSeriesWithGenres
import com.koniukhov.cinecirclex.core.database.dao.RatedMediaDao
import com.koniukhov.cinecirclex.core.database.entity.RatedMediaEntity
import com.koniukhov.cinecirclex.core.database.entity.MediaListEntity
import com.koniukhov.cinecirclex.core.database.model.MediaListWithCount
import com.koniukhov.cinecirclex.core.database.repository.MediaListRepository
import com.koniukhov.cinecirclex.core.domain.usecase.GetSimilarTvSeriesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeasonDetails
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesContentRatingsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesCreditsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesDetailsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesImagesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesRecommendationUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesReviewsUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesVideosUseCase
import com.koniukhov.cinecirclex.feature.media.details.ui.state.TvSeriesDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
class TvSeriesDetailsViewModel @Inject constructor(
    private val getTvSeriesDetailsUseCase: GetTvSeriesDetailsUseCase,
    private val getTvSeasonDetails: GetTvSeasonDetails,
    private val getTvSeriesImagesUseCase: GetTvSeriesImagesUseCase,
    private val getTvSeriesVideosUseCase: GetTvSeriesVideosUseCase,
    private val getTvSeriesReviewsUseCase: GetTvSeriesReviewsUseCase,
    private val getTvSeriesCreditsUseCase: GetTvSeriesCreditsUseCase,
    private val getTvSeriesContentRatingsUseCase: GetTvSeriesContentRatingsUseCase,
    private val getTvSeriesRecommendationUseCase: GetTvSeriesRecommendationUseCase,
    private val getSimilarTvSeriesUseCase: GetSimilarTvSeriesUseCase,
    private val mediaListRepository: MediaListRepository,
    @LanguageCode
    private val languageCode: String,
    @CountryCode
    val countryCode: String,
    private val ratedMediaDao: RatedMediaDao
): ViewModel(){
    private var _tvSeriesId = MutableStateFlow(INVALID_ID)
    private var _uiState = MutableStateFlow(TvSeriesDetailsUiState())
    val uiState: StateFlow<TvSeriesDetailsUiState> = _uiState.asStateFlow()

    private val _mediaCollection = MutableStateFlow<MediaListEntity?>(null)

    val isTvSeriesInCollection: StateFlow<Boolean> = _mediaCollection.asStateFlow().map { it != null }.stateIn(
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

    fun initTvSeries(id: Int) {
        setTvSeriesId(id)
        loadUserRating()
    }
    fun setTvSeriesId(id: Int) {
        _tvSeriesId.value = id
    }

    fun loadTvSeriesDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val details = getTvSeriesDetailsUseCase(_tvSeriesId.value, languageCode)

                val seasonsDeferred = details.seasons.map { season ->
                    async { getTvSeasonDetails(_tvSeriesId.value, season.seasonNumber, languageCode) }
                }
                val imagesDeferred = async {
                    var images = getTvSeriesImagesUseCase(details.id, languageCode)
                    if ((images.posters.isEmpty() && images.backdrops.isEmpty()) && languageCode != ENGLISH_LANGUAGE_CODE) {
                        images = getTvSeriesImagesUseCase(details.id, ENGLISH_LANGUAGE_CODE)
                    }
                    images
                }

                val videosDeferred = async {
                    var videos = getTvSeriesVideosUseCase(details.id, languageCode)
                    if (videos.results.isEmpty() && languageCode != ENGLISH_LANGUAGE_CODE) {
                        videos = getTvSeriesVideosUseCase(details.id, ENGLISH_LANGUAGE_CODE)
                    }
                    videos
                }
                val reviewsDeferred = async { getTvSeriesReviewsUseCase(_tvSeriesId.value, languageCode, 1) }
                val creditsDeferred = async { getTvSeriesCreditsUseCase(_tvSeriesId.value, languageCode) }
                val contentRatingsDeferred = async { getTvSeriesContentRatingsUseCase(_tvSeriesId.value) }
                val recommendationsDeferred = async { getTvSeriesRecommendationUseCase(_tvSeriesId.value, languageCode, 1) }
                val similarDeferred = async { getSimilarTvSeriesUseCase(_tvSeriesId.value, languageCode, 1) }

                val seasons = seasonsDeferred.awaitAll()
                val images = imagesDeferred.await()
                val videos = videosDeferred.await()
                val reviews = reviewsDeferred.await()
                val credits = creditsDeferred.await()
                val contentRatings = contentRatingsDeferred.await()
                val recommendations = recommendationsDeferred.await()
                val similar = similarDeferred.await()

                _uiState.value = TvSeriesDetailsUiState(
                    isLoading = false,
                    error = null,
                    details = details,
                    seasons = seasons,
                    images = images,
                    videos = videos,
                    reviews = reviews,
                    credits = credits,
                    contentRatings = contentRatings,
                    recommendations = recommendations,
                    similar = similar
                )
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun checkAndLoadCollections(){
        checkMediaCollection()
        loadAllCollections()
    }

    private fun checkMediaCollection() {
        viewModelScope.launch {
            try {
                val collections = mediaListRepository.getListsContainingTvSeries(_tvSeriesId.value)
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

    suspend fun addTvSeriesToCollection(collectionId: Long): String {
        return try {

            val collection = mediaListRepository.getListById(collectionId)
            val collectionName = collection?.name ?: ""

            val success = mediaListRepository.addTvSeriesToList(collectionId, _tvSeriesId.value)
            if (success) {
                checkMediaCollection()
            }
            collectionName
        } catch (e: Exception) {
            Timber.e(e)
            ""
        }
    }

    fun removeTvSeriesFromCollection() {
        viewModelScope.launch {
            try {
                _mediaCollection.value?.let { currentCollection ->
                    mediaListRepository.removeTvSeriesFromList(currentCollection.id, _tvSeriesId.value)
                    checkMediaCollection()
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun cacheTvSeriesDetails() {
        val details = _uiState.value.details
        viewModelScope.launch {
            details?.let { details ->
                mediaListRepository.insertTvSeriesWithGenres(details.toTvSeriesWithGenres(_tvSeriesId.value))
            }
        }
    }

    fun removeTvSeriesDetailsFromCache() {
        viewModelScope.launch {
            mediaListRepository.deleteTvSeriesWithGenres(_tvSeriesId.value)
        }
    }

    fun loadUserRating() {
        val id = _tvSeriesId.value
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
        val id = _tvSeriesId.value
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