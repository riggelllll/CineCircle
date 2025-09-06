package com.koniukhov.cinecircle.feature.movie.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koniukhov.cinecircle.core.data.di.CountryCode
import com.koniukhov.cinecircle.core.data.di.LanguageCode
import com.koniukhov.cinecircle.core.domain.usecase.GetCollectionDetailsUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetMovieCreditsUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetMovieDetailsUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetMovieImagesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetMovieRecommendationsUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetMovieReleaseDatesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetMovieReviewsUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetMovieVideosUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetSimilarMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
    @LanguageCode
    private val languageCode: String,
    @CountryCode
    val countryCode: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()
    private var _movieId = MutableStateFlow(-1)
    val movieId: StateFlow<Int> = _movieId.asStateFlow()

    fun setMovieId(id: Int) {
        _movieId.value = id
    }

    fun loadMovieDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val movieDetails = getMovieDetailsUseCase(_movieId.value, languageCode)
                val collectionDetails = getCollectionDetailsUseCase(movieDetails.belongsToCollection.id, languageCode)
                val images = getMovieImagesUseCase(movieDetails.id, languageCode)
                val videos = getMovieVideosUseCase(movieDetails.id, languageCode)
                val reviews = getMovieReviewsUseCase(movieDetails.id, 1, languageCode)
                val credits = getMovieCreditsUseCase(movieDetails.id, languageCode)
                val recommendations = getMovieRecommendationsUseCase(movieDetails.id, 1, languageCode)
                val similarMovies = getSimilarMoviesUseCase(movieDetails.id, 1, languageCode)
                val releaseDates = getMovieReleaseDatesUseCase(movieDetails.id)
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
}