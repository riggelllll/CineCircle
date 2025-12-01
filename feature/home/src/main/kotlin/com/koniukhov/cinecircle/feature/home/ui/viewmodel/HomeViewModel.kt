package com.koniukhov.cinecircle.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koniukhov.cinecircle.core.data.di.LanguageCode
import com.koniukhov.cinecircle.core.design.util.getMoviesGenreUiList
import com.koniukhov.cinecircle.core.design.util.getTvSeriesGenreUiList
import com.koniukhov.cinecircle.core.domain.usecase.GetAiringTodayTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetMovieGenresUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetNowPlayingMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetOnAirTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetPopularMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetPopularTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTopRatedMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTopRatedTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTrendingMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTrendingTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeriesGenresUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetUpcomingMoviesUseCase
import com.koniukhov.cinecircle.feature.home.ui.state.MoviesUiState
import com.koniukhov.cinecircle.feature.home.ui.state.TvSeriesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @LanguageCode
    private val languageCode: String,
    private val getMovieGenresUseCase: GetMovieGenresUseCase,
    private val getTvSeriesGenresUseCase: GetTvSeriesGenresUseCase,
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getAiringTodayTvSeriesUseCase: GetAiringTodayTvSeriesUseCase,
    private val getOnAirTvSeriesUseCase: GetOnAirTvSeriesUseCase,
    private val getTrendingTvSeriesUseCase: GetTrendingTvSeriesUseCase,
    private val getPopularTvSeriesUseCase: GetPopularTvSeriesUseCase,
    private val getTopRatedTvSeriesUseCase: GetTopRatedTvSeriesUseCase
) : ViewModel() {
    private val _moviesUiState = MutableStateFlow(MoviesUiState())
    val moviesUiState: StateFlow<MoviesUiState> = _moviesUiState
    private val _tvSeriesUiState = MutableStateFlow(TvSeriesUiState())
    val tvSeriesUiState: StateFlow<TvSeriesUiState> = _tvSeriesUiState

    fun loadMoviesForAllCategories(page: Int = 1) {
        if (_moviesUiState.value.trendingMovies.isNotEmpty()) {
            return
        }

        viewModelScope.launch {
            _moviesUiState.value = _moviesUiState.value.copy(isLoading = true, error = null)
            try {
                val movieGenresDeferred = async { getMovieGenresUseCase(languageCode) }
                val trendingMoviesDeferred = async { getTrendingMoviesUseCase(page, languageCode) }
                val nowPlayingMoviesDeferred = async { getNowPlayingMoviesUseCase(page, languageCode) }
                val popularMoviesDeferred = async { getPopularMoviesUseCase(page, languageCode) }
                val topRatedMoviesDeferred = async { getTopRatedMoviesUseCase(page, languageCode) }
                val upcomingMoviesDeferred = async { getUpcomingMoviesUseCase(page, languageCode) }

                val movieGenres = movieGenresDeferred.await()
                val trendingMovies = trendingMoviesDeferred.await()
                val nowPlayingMovies = nowPlayingMoviesDeferred.await()
                val popularMovies = popularMoviesDeferred.await()
                val topRatedMovies = topRatedMoviesDeferred.await()
                val upcomingMovies = upcomingMoviesDeferred.await()

                _moviesUiState.value = _moviesUiState.value.copy(
                    trendingMovies = trendingMovies,
                    nowPlayingMovies = nowPlayingMovies,
                    popularMovies = popularMovies,
                    topRatedMovies = topRatedMovies,
                    upcomingMovies = upcomingMovies,
                    genreUiMovies = getMoviesGenreUiList(movieGenres),
                    isLoading = false
                )
            } catch (e: Exception) {
                _moviesUiState.value = _moviesUiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun loadTvSeriesForAllCategories(page: Int = 1) {
        if (_tvSeriesUiState.value.trendingTvSeries.isNotEmpty()) {
            return
        }

        viewModelScope.launch {
            _tvSeriesUiState.value = _tvSeriesUiState.value.copy(isLoading = true, error = null)
            try {
                val tvSeriesGenresDeferred = async { getTvSeriesGenresUseCase(languageCode) }
                val airingTodayTvSeriesDeferred = async { getAiringTodayTvSeriesUseCase(page, languageCode) }
                val onTheAirTvSeriesDeferred = async { getOnAirTvSeriesUseCase(page, languageCode) }
                val trendingTvSeriesDeferred = async { getTrendingTvSeriesUseCase(page, languageCode) }
                val popularTvSeriesDeferred = async { getPopularTvSeriesUseCase(page, languageCode) }
                val topRatedTvSeriesDeferred = async { getTopRatedTvSeriesUseCase(page, languageCode) }

                val tvSeriesGenres = tvSeriesGenresDeferred.await()
                val airingTodayTvSeries = airingTodayTvSeriesDeferred.await()
                val onTheAirTvSeries = onTheAirTvSeriesDeferred.await()
                val trendingTvSeries = trendingTvSeriesDeferred.await()
                val popularTvSeries = popularTvSeriesDeferred.await()
                val topRatedTvSeries = topRatedTvSeriesDeferred.await()

                _tvSeriesUiState.value = _tvSeriesUiState.value.copy(
                    airingTodayTvSeries = airingTodayTvSeries,
                    onTheAirTvSeries = onTheAirTvSeries,
                    trendingTvSeries = trendingTvSeries,
                    popularTvSeries = popularTvSeries,
                    topRatedTvSeries = topRatedTvSeries,
                    genreUiTvSeries = getTvSeriesGenreUiList(tvSeriesGenres),
                    isLoading = false
                )
            } catch (e: Exception) {
                _tvSeriesUiState.value = _tvSeriesUiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}