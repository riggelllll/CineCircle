package com.koniukhov.cinecircle.feature.media.details.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID
import com.koniukhov.cinecircle.core.data.di.LanguageCode
import com.koniukhov.cinecircle.core.domain.usecase.GetSimilarTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeasonDetails
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeriesContentRatingsUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeriesCreditsUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeriesDetailsUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeriesImagesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeriesRecommendationUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeriesReviewsUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeriesVideosUseCase
import com.koniukhov.cinecircle.feature.media.details.ui.state.TvSeriesDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
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
    @LanguageCode
    private val languageCode: String,
): ViewModel(){
    private var _tvSeriesId = MutableStateFlow(INVALID_ID)
    private var _uiState = MutableStateFlow(TvSeriesDetailsUiState())
    val uiState: StateFlow<TvSeriesDetailsUiState> = _uiState.asStateFlow()

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
                val imagesDeferred = async { getTvSeriesImagesUseCase(_tvSeriesId.value, languageCode) }
                val videosDeferred = async { getTvSeriesVideosUseCase(_tvSeriesId.value, languageCode) }
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
}