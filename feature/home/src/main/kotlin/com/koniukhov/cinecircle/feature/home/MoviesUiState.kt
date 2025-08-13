package com.koniukhov.cinecircle.feature.home

import com.koniukhov.cinecircle.core.domain.model.Movie

data class MoviesUiState(
    val nowPlayingMovies: List<Movie> = emptyList(),
    val popularMovies: List<Movie> = emptyList(),
    val topRatedMovies: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)