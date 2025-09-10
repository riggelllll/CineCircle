package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.domain.datasource.TvSeriesDataSource
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
}