package com.koniukhov.cinecircle.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koniukhov.cinecircle.core.design.util.moviesGenreUiList
import com.koniukhov.cinecircle.core.domain.usecase.GetNowPlayingMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetPopularMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTopRatedMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTrendingMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetUpcomingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState

    fun loadMoviesForAllCategories(page: Int = 1) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val trendingMovies = getTrendingMoviesUseCase(page)
                val nowPlayingMovies = getNowPlayingMoviesUseCase(page)
                val popularMovies = getPopularMoviesUseCase(page)
                val topRatedMovies = getTopRatedMoviesUseCase(page)
                val upcomingMovies = getUpcomingMoviesUseCase(page)
                _uiState.value = _uiState.value.copy(
                    trendingMovies = trendingMovies,
                    nowPlayingMovies = nowPlayingMovies,
                    popularMovies = popularMovies,
                    topRatedMovies = topRatedMovies,
                    upcomingMovies = upcomingMovies,
                    genreUiMovies = moviesGenreUiList,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}