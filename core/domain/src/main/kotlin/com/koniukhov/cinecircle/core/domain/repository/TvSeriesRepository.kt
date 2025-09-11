package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.model.TvSeriesDetails

interface TvSeriesRepository {
    suspend fun getAiringTodayTvSeries(page: Int = 1, language: String): List<TvSeries>
    suspend fun getOnTheAirTvSeries(page: Int = 1, language: String): List<TvSeries>
    suspend fun getTrendingTvSeries(page: Int = 1, language: String): List<TvSeries>
    suspend fun getPopularTvSeries(page: Int = 1, language: String): List<TvSeries>
    suspend fun getTopRatedTvSeries(page: Int = 1, language: String): List<TvSeries>
    suspend fun getTvSeriesByGenre(genreId: Int, page: Int = 1, language: String): List<TvSeries>
    suspend fun getTvSeriesDetails(id: Int, language: String): TvSeriesDetails
    suspend fun getTvSeasonDetails(tvSeriesId: Int, seasonNumber: Int, language: String): TvSeasonDetails
    suspend fun getTvSeriesRecommendations(tvSeriesId: Int, page: Int = 1, language: String): List<TvSeries>
    suspend fun getSimilarTvSeries(tvSeriesId: Int, page: Int = 1, language: String): List<TvSeries>
}