package com.koniukhov.cinecirclex.feature.home.ui.state

import com.koniukhov.cinecirclex.core.common.model.GenreUi
import com.koniukhov.cinecirclex.core.domain.model.TvSeries

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