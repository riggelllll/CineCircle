package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.domain.datasource.TvSeriesDataSource
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.TvSeriesResponseDto
import javax.inject.Inject

class RemoteTvSeriesDataSource @Inject constructor(private val api: TMDBApi) : TvSeriesDataSource {

    override suspend fun getAiringTodayTvSeries(page: Int): TvSeriesResponseDto {
        return api.getAiringTodayTvSeries(BuildConfig.API_KEY, page).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getOnTheAirTvSeries(page: Int): TvSeriesResponseDto {
        return api.getOnTheAirTvSeries(BuildConfig.API_KEY, page).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getTrendingTvSeries(page: Int): TvSeriesResponseDto {
        return api.getTrendingTvSeries(BuildConfig.API_KEY, page).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getPopularTvSeries(page: Int): TvSeriesResponseDto {
        return api.getPopularTvSeries(BuildConfig.API_KEY, page).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getTopRatedTvSeries(page: Int): TvSeriesResponseDto {
        return api.getTopRatedTvSeries(BuildConfig.API_KEY, page).body() ?: TvSeriesResponseDto.empty()
    }
}