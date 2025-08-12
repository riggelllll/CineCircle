package com.koniukhov.cinecircle.feature.home

import com.koniukhov.cinecircle.core.domain.model.Movie

data class MoviesUiState(
    val popularMovies: List<Movie> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)