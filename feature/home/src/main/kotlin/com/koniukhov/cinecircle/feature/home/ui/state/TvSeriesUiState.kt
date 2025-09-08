package com.koniukhov.cinecircle.feature.home.ui.state

import com.koniukhov.cinecircle.core.common.model.GenreUi
import com.koniukhov.cinecircle.core.domain.model.TvSeries

data class TvSeriesUiState (
    val airingTodayTvSeries: List<TvSeries> = emptyList(),
    val onTheAirTvSeries: List<TvSeries> = emptyList(),
    val trendingTvSeries: List<TvSeries> = emptyList(),
    val popularTvSeries: List<TvSeries> = emptyList(),
    val topRatedTvSeries: List<TvSeries> = emptyList(),
    val genreUiTvSeries: List<GenreUi> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)