package com.koniukhov.cinecircle.feature.home.ui.state

import com.koniukhov.cinecircle.core.common.model.GenreUi
import com.koniukhov.cinecircle.core.domain.model.Movie

data class MoviesUiState(
    val trendingMovies: List<Movie> = emptyList(),
    val nowPlayingMovies: List<Movie> = emptyList(),
    val popularMovies: List<Movie> = emptyList(),
    val topRatedMovies: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList(),
    val genreUiMovies: List<GenreUi> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)