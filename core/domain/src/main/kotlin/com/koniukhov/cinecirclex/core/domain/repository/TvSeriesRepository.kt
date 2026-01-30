package com.koniukhov.cinecirclex.core.domain.repository

import com.koniukhov.cinecirclex.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecirclex.core.domain.model.TvSeries
import com.koniukhov.cinecirclex.core.domain.model.TvSeriesDetails

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
    suspend fun getSearchedTvSeries(query: String, page: Int = 1, language: String): List<TvSeries>
    suspend fun getFilteredTvSeries(
        page: Int = 1,
        language: String,
        sortBy: String,
        airDateGte: String? = null,
        airDateLte: String? = null,
        year: Int? = null,
        firstAirDateGte: String? = null,
        firstAirDateLte: String? = null,
        minVoteAverage: Float? = null,
        maxVoteAverage: Float? = null,
        minVoteCount: Int? = null,
        maxVoteCount: Int? = null,
        withOriginCountry: String? = null,
        withOriginalLanguage: String? = null,
        withGenres: String? = null,
        withoutGenres: String? = null
    ): List<TvSeries>
}