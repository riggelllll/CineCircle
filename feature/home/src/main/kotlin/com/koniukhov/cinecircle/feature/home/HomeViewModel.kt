package com.koniukhov.cinecircle.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koniukhov.cinecircle.core.design.util.moviesGenreUiList
import com.koniukhov.cinecircle.core.design.util.tvSeriesGenreUiList
import com.koniukhov.cinecircle.core.domain.usecase.GetAiringTodayTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetNowPlayingMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetOnAirTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetPopularMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetPopularTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTopRatedMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTopRatedTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTrendingMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTrendingTvSeriesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetUpcomingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val language: String,
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
        viewModelScope.launch {
            _moviesUiState.value = _moviesUiState.value.copy(isLoading = true, error = null)
            try {
                val trendingMovies = getTrendingMoviesUseCase(page, language)
                val nowPlayingMovies = getNowPlayingMoviesUseCase(page, language)
                val popularMovies = getPopularMoviesUseCase(page, language)
                val topRatedMovies = getTopRatedMoviesUseCase(page, language)
                val upcomingMovies = getUpcomingMoviesUseCase(page, language)
                _moviesUiState.value = _moviesUiState.value.copy(
                    trendingMovies = trendingMovies,
                    nowPlayingMovies = nowPlayingMovies,
                    popularMovies = popularMovies,
                    topRatedMovies = topRatedMovies,
                    upcomingMovies = upcomingMovies,
                    genreUiMovies = moviesGenreUiList,
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
        viewModelScope.launch {
            _tvSeriesUiState.value = _tvSeriesUiState.value.copy(isLoading = true, error = null)
            try {
                val airingTodayTvSeries = getAiringTodayTvSeriesUseCase(page, language)
                val onTheAirTvSeries = getOnAirTvSeriesUseCase(page, language)
                val trendingTvSeries = getTrendingTvSeriesUseCase(page, language)
                val popularTvSeries = getPopularTvSeriesUseCase(page, language)
                val topRatedTvSeries = getTopRatedTvSeriesUseCase(page, language)
                _tvSeriesUiState.value = _tvSeriesUiState.value.copy(
                    airingTodayTvSeries = airingTodayTvSeries,
                    onTheAirTvSeries = onTheAirTvSeries,
                    trendingTvSeries = trendingTvSeries,
                    popularTvSeries = popularTvSeries,
                    topRatedTvSeries = topRatedTvSeries,
                    genreUiTvSeries = tvSeriesGenreUiList,
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