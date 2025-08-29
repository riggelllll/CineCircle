package com.koniukhov.cinecircle.feature.movie.details

import com.koniukhov.cinecircle.core.domain.model.CollectionDetails
import com.koniukhov.cinecircle.core.domain.model.MediaImages
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.MovieCredits
import com.koniukhov.cinecircle.core.domain.model.MovieDetails
import com.koniukhov.cinecircle.core.domain.model.MovieReview
import com.koniukhov.cinecircle.core.domain.model.MovieVideos

data class MovieDetailsUiState(
    val isLoading: Boolean = false,
    val movieDetails: MovieDetails? = null,
    val collectionDetails: CollectionDetails? = null,
    val images: MediaImages? = null,
    val videos: MovieVideos? = null,
    val reviews: List<MovieReview> = emptyList(),
    val credits: MovieCredits? = null,
    val recommendations: List<Movie> = emptyList(),
    val similarMovies: List<Movie> = emptyList(),
    val error: String? = null
)