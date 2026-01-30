package com.koniukhov.cinecirclex.feature.media.details.ui.state

import com.koniukhov.cinecirclex.core.domain.model.ContentRating
import com.koniukhov.cinecirclex.core.domain.model.MediaCredits
import com.koniukhov.cinecirclex.core.domain.model.MediaImages
import com.koniukhov.cinecirclex.core.domain.model.MediaReview
import com.koniukhov.cinecirclex.core.domain.model.MediaVideos
import com.koniukhov.cinecirclex.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecirclex.core.domain.model.TvSeries
import com.koniukhov.cinecirclex.core.domain.model.TvSeriesDetails

data class TvSeriesDetailsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val details: TvSeriesDetails? = null,
    val seasons: List<TvSeasonDetails> = emptyList(),
    val images: MediaImages? = null,
    val videos: MediaVideos? = null,
    val reviews: List<MediaReview> = emptyList(),
    val credits: MediaCredits? = null,
    val contentRatings: List<ContentRating> = emptyList(),
    val recommendations: List<TvSeries> = emptyList(),
    val similar: List<TvSeries> = emptyList(),
)