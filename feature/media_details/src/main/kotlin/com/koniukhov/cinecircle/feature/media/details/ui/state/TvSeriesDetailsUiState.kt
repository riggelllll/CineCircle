package com.koniukhov.cinecircle.feature.media.details.ui.state

import com.koniukhov.cinecircle.core.domain.model.ContentRating
import com.koniukhov.cinecircle.core.domain.model.MediaCredits
import com.koniukhov.cinecircle.core.domain.model.MediaImages
import com.koniukhov.cinecircle.core.domain.model.MediaReview
import com.koniukhov.cinecircle.core.domain.model.MediaVideos
import com.koniukhov.cinecircle.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.model.TvSeriesDetails

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