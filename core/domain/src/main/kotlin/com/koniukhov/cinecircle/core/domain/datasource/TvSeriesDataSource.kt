package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.TvSeriesResponseDto

interface TvSeriesDataSource {
    suspend fun getAiringTodayTvSeries(page: Int): TvSeriesResponseDto
    suspend fun getOnTheAirTvSeries(page: Int): TvSeriesResponseDto
    suspend fun getTrendingTvSeries(page: Int): TvSeriesResponseDto
    suspend fun getPopularTvSeries(page: Int): TvSeriesResponseDto
    suspend fun getTopRatedTvSeries(page: Int): TvSeriesResponseDto
}