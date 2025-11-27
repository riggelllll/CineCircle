package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.TvSeasonDetailsDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesDetailsDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesResponseDto
import javax.inject.Inject

class RemoteTvSeriesDataSourceImpl @Inject constructor(private val api: TMDBApi) : RemoteTvSeriesDataSource {

    override suspend fun getAiringTodayTvSeries(page: Int, language: String): TvSeriesResponseDto {
        return api.getAiringTodayTvSeries(BuildConfig.API_KEY, page, language).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getOnTheAirTvSeries(page: Int, language: String): TvSeriesResponseDto {
        return api.getOnTheAirTvSeries(BuildConfig.API_KEY, page, language).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getTrendingTvSeries(page: Int, language: String): TvSeriesResponseDto {
        return api.getTrendingTvSeries(BuildConfig.API_KEY, page, language).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getPopularTvSeries(page: Int, language: String): TvSeriesResponseDto {
        return api.getPopularTvSeries(BuildConfig.API_KEY, page, language).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getTopRatedTvSeries(page: Int, language: String): TvSeriesResponseDto {
        return api.getTopRatedTvSeries(BuildConfig.API_KEY, page, language).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getTvSeriesByGenre(
        genreId: Int,
        page: Int,
        language: String
    ): TvSeriesResponseDto {
        return api.getTvSeriesByGenre(BuildConfig.API_KEY, genreId, page, language).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getTvSeriesDetails(
        id: Int,
        language: String
    ): TvSeriesDetailsDto {
        return api.getTvSeriesDetails(id, BuildConfig.API_KEY, language).body() ?: TvSeriesDetailsDto.empty()
    }

    override suspend fun getTvSeasonDetails(
        tvSeriesId: Int,
        seasonNumber: Int,
        language: String
    ): TvSeasonDetailsDto {
        return api.getTvSeasonDetails(tvSeriesId, seasonNumber, BuildConfig.API_KEY, language).body() ?: TvSeasonDetailsDto.empty()
    }

    override suspend fun getTvSeriesRecommendations(
        id: Int,
        page: Int,
        language: String
    ): TvSeriesResponseDto {
        return api.getTvSeriesRecommendations(id, BuildConfig.API_KEY, page, language).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getSimilarTvSeries(
        id: Int,
        page: Int,
        language: String
    ): TvSeriesResponseDto {
        return api.getSimilarTvSeries(id, BuildConfig.API_KEY, page, language).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getSearchedTvSeries(
        query: String,
        page: Int,
        language: String
    ): TvSeriesResponseDto {
        return api.getSearchedTvSeries(BuildConfig.API_KEY, query, page, language).body() ?: TvSeriesResponseDto.empty()
    }

    override suspend fun getFilteredTvSeries(
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
    ): TvSeriesResponseDto {
        return api.getFilteredTvSeries(
            BuildConfig.API_KEY,
            page,
            language,
            sortBy,
            airDateGte,
            airDateLte,
            year,
            firstAirDateGte,
            firstAirDateLte,
            minVoteAverage,
            maxVoteAverage,
            minVoteCount,
            maxVoteCount,
            withOriginCountry,
            withOriginalLanguage,
            withGenres,
            withoutGenres
        ).body() ?: TvSeriesResponseDto.empty()
    }
}