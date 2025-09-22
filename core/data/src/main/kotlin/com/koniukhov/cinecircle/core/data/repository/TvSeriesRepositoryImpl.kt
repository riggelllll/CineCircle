package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.domain.datasource.TvSeriesDataSource
import com.koniukhov.cinecircle.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.model.TvSeriesDetails
import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import javax.inject.Inject

class TvSeriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: TvSeriesDataSource
) : TvSeriesRepository{
    override suspend fun getAiringTodayTvSeries(page: Int, language: String): List<TvSeries> {
        val dto = remoteDataSource.getAiringTodayTvSeries(page, language)
        return dto.results.map { it.toDomain() }
    }
    override suspend fun getOnTheAirTvSeries(page: Int, language: String): List<TvSeries> {
        val dto = remoteDataSource.getOnTheAirTvSeries(page, language)
        return dto.results.map { it.toDomain() }
    }
    override suspend fun getTrendingTvSeries(page: Int, language: String): List<TvSeries> {
        val dto = remoteDataSource.getTrendingTvSeries(page, language)
        return dto.results.map { it.toDomain() }
    }
    override suspend fun getPopularTvSeries(page: Int, language: String): List<TvSeries> {
        val dto = remoteDataSource.getPopularTvSeries(page, language)
        return dto.results.map { it.toDomain() }
    }
    override suspend fun getTopRatedTvSeries(page: Int, language: String): List<TvSeries> {
        val dto = remoteDataSource.getTopRatedTvSeries(page, language)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getTvSeriesByGenre(
        genreId: Int,
        page: Int,
        language: String
    ): List<TvSeries> {
        val dto = remoteDataSource.getTvSeriesByGenre(genreId, page, language)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getTvSeriesDetails(
        id: Int,
        language: String
    ): TvSeriesDetails {
        val dto = remoteDataSource.getTvSeriesDetails(id, language)
        return dto.toDomain()
    }

    override suspend fun getTvSeasonDetails(
        tvSeriesId: Int,
        seasonNumber: Int,
        language: String
    ): TvSeasonDetails {
        val dto = remoteDataSource.getTvSeasonDetails(tvSeriesId, seasonNumber, language)
        return dto.toDomain()
    }

    override suspend fun getTvSeriesRecommendations(
        tvSeriesId: Int,
        page: Int,
        language: String
    ): List<TvSeries> {
        val dto = remoteDataSource.getTvSeriesRecommendations(tvSeriesId, page, language)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getSimilarTvSeries(
        tvSeriesId: Int,
        page: Int,
        language: String
    ): List<TvSeries> {
        val dto = remoteDataSource.getSimilarTvSeries(tvSeriesId, page, language)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getSearchedTvSeries(
        query: String,
        page: Int,
        language: String
    ): List<TvSeries> {
        val dto = remoteDataSource.getSearchedTvSeries(query, page, language)
        return dto.results.map { it.toDomain() }
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
    ): List<TvSeries> {
        val dto = remoteDataSource.getFilteredTvSeries(
            page = page,
            language = language,
            sortBy = sortBy,
            airDateGte = airDateGte,
            airDateLte = airDateLte,
            year = year,
            firstAirDateGte = firstAirDateGte,
            firstAirDateLte = firstAirDateLte,
            minVoteAverage = minVoteAverage,
            maxVoteAverage = maxVoteAverage,
            minVoteCount = minVoteCount,
            maxVoteCount = maxVoteCount,
            withOriginCountry = withOriginCountry,
            withOriginalLanguage = withOriginalLanguage,
            withGenres = withGenres,
            withoutGenres = withoutGenres
        )
        return dto.results.map { it.toDomain() }
    }
}