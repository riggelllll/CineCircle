package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.network.model.TvSeasonDetailsDto
import com.koniukhov.cinecirclex.core.network.model.TvSeriesDetailsDto
import com.koniukhov.cinecirclex.core.network.model.TvSeriesResponseDto

interface RemoteTvSeriesDataSource {
    suspend fun getAiringTodayTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getOnTheAirTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getTrendingTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getPopularTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getTopRatedTvSeries(page: Int, language: String): TvSeriesResponseDto
    suspend fun getTvSeriesByGenre(genreId: Int, page: Int, language: String): TvSeriesResponseDto
    suspend fun getTvSeriesDetails(id: Int, language: String): TvSeriesDetailsDto
    suspend fun getTvSeasonDetails(tvSeriesId: Int, seasonNumber: Int, language: String): TvSeasonDetailsDto
    suspend fun getTvSeriesRecommendations(id: Int, page: Int, language: String): TvSeriesResponseDto
    suspend fun getSimilarTvSeries(id: Int, page: Int, language: String): TvSeriesResponseDto
    suspend fun getSearchedTvSeries(query: String, page: Int, language: String): TvSeriesResponseDto
    suspend fun getFilteredTvSeries(
        page: Int,
        language: String,
        sortBy: String,
        airDateGte: String?,
        airDateLte: String?,
        year: Int?,
        firstAirDateGte: String?,
        firstAirDateLte: String?,
        minVoteAverage: Float?,
        maxVoteAverage: Float?,
        minVoteCount: Int?,
        maxVoteCount: Int?,
        withOriginCountry: String?,
        withOriginalLanguage: String?,
        withGenres: String?,
        withoutGenres: String?
    ): TvSeriesResponseDto
}