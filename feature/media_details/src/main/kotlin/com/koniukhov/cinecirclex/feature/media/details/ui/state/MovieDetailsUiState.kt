package com.koniukhov.cinecirclex.feature.media.details.ui.state

import com.koniukhov.cinecirclex.core.domain.model.CollectionDetails
import com.koniukhov.cinecirclex.core.domain.model.MediaImages
import com.koniukhov.cinecirclex.core.domain.model.Movie
import com.koniukhov.cinecirclex.core.domain.model.MediaCredits
import com.koniukhov.cinecirclex.core.domain.model.MovieDetails
import com.koniukhov.cinecirclex.core.domain.model.MediaReview
import com.koniukhov.cinecirclex.core.domain.model.MediaVideos
import com.koniukhov.cinecirclex.core.domain.model.ReleaseDateResult

data class MovieDetailsUiState(
    val isLoading: Boolean = false,
    val movieDetails: MovieDetails? = null,
    val collectionDetails: CollectionDetails? = null,
    val images: MediaImages? = null,
    val videos: MediaVideos? = null,
    val reviews: List<MediaReview> = emptyList(),
    val credits: MediaCredits? = null,
    val recommendations: List<Movie> = emptyList(),
    val similarMovies: List<Movie> = emptyList(),
    val releaseDates: List<ReleaseDateResult> = emptyList(),
    val error: String? = null
)