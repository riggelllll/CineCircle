package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.local.LocalTvSeriesDataSource
import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.mapper.toTvSeriesDetails
import com.koniukhov.cinecircle.core.data.remote.RemoteTvSeriesDataSource
import com.koniukhov.cinecircle.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.model.TvSeriesDetails
import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import javax.inject.Inject

class TvSeriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteTvSeriesDataSource,
    private val localDataSource: LocalTvSeriesDataSource,
    private val networkStatusProvider: NetworkStatusProvider
) : TvSeriesRepository{
    override suspend fun getAiringTodayTvSeries(page: Int, language: String): List<TvSeries> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getAiringTodayTvSeries(page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }
    override suspend fun getOnTheAirTvSeries(page: Int, language: String): List<TvSeries> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getOnTheAirTvSeries(page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }
    override suspend fun getTrendingTvSeries(page: Int, language: String): List<TvSeries> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getTrendingTvSeries(page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }
    override suspend fun getPopularTvSeries(page: Int, language: String): List<TvSeries> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getPopularTvSeries(page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }
    override suspend fun getTopRatedTvSeries(page: Int, language: String): List<TvSeries> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getTopRatedTvSeries(page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getTvSeriesByGenre(
        genreId: Int,
        page: Int,
        language: String
    ): List<TvSeries> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getTvSeriesByGenre(genreId, page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getTvSeriesDetails(
        id: Int,
        language: String
    ): TvSeriesDetails {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getTvSeriesDetails(id, language)
                dto.toDomain() },
            localCall = { localDataSource.getTvSeriesWithGenres(id)?.toTvSeriesDetails() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: TvSeriesDetails.empty()
    }

    override suspend fun getTvSeasonDetails(
        tvSeriesId: Int,
        seasonNumber: Int,
        language: String
    ): TvSeasonDetails {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getTvSeasonDetails(tvSeriesId, seasonNumber, language)
                dto.toDomain() },
            localCall = { TvSeasonDetails.empty() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: TvSeasonDetails.empty()
    }

    override suspend fun getTvSeriesRecommendations(
        tvSeriesId: Int,
        page: Int,
        language: String
    ): List<TvSeries> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getTvSeriesRecommendations(tvSeriesId, page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { emptyList() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getSimilarTvSeries(
        tvSeriesId: Int,
        page: Int,
        language: String
    ): List<TvSeries> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getSimilarTvSeries(tvSeriesId, page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { emptyList() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getSearchedTvSeries(
        query: String,
        page: Int,
        language: String
    ): List<TvSeries> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getSearchedTvSeries(query, page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
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
        return fetchWithLocalAndRetry(
            remoteCall = {
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
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }
}