package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.TvSeasonDetailsDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesDetailsDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesResponseDto

interface TvSeriesDataSource {
    suspend fun getAiringTodayTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getOnTheAirTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getTrendingTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getPopularTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getTopRatedTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getTvSeriesByGenre(genreId: Int, page: Int, language: String): TvSeriesResponseDto
    suspend fun getTvSeriesDetails(id: Int, language: String): TvSeriesDetailsDto
    suspend fun getTvSeasonDetails(tvSeriesId: Int, seasonNumber: Int, language: String): TvSeasonDetailsDto
    suspend fun getTvSeriesRecommendations(id: Int, page: Int, language: String): TvSeriesResponseDto
}