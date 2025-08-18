package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.TvSeriesResponseDto

interface TvSeriesDataSource {
    suspend fun getAiringTodayTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getOnTheAirTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getTrendingTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getPopularTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getTopRatedTvSeries(page: Int, language: String): TvSeriesResponseDto
}