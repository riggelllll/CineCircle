package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.domain.datasource.TvSeriesDataSource
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import javax.inject.Inject

class TvSeriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: TvSeriesDataSource
) : TvSeriesRepository{
    override suspend fun getAiringTodayTvSeries(page: Int): List<TvSeries> {
        val dto = remoteDataSource.getAiringTodayTvSeries(page)
        return dto.results.map { it.toDomain() }
    }
    override suspend fun getOnTheAirTvSeries(page: Int): List<TvSeries> {
        val dto = remoteDataSource.getOnTheAirTvSeries(page)
        return dto.results.map { it.toDomain() }
    }
    override suspend fun getTrendingTvSeries(page: Int): List<TvSeries> {
        val dto = remoteDataSource.getTrendingTvSeries(page)
        return dto.results.map { it.toDomain() }
    }
    override suspend fun getPopularTvSeries(page: Int): List<TvSeries> {
        val dto = remoteDataSource.getPopularTvSeries(page)
        return dto.results.map { it.toDomain() }
    }
    override suspend fun getTopRatedTvSeries(page: Int): List<TvSeries> {
        val dto = remoteDataSource.getTopRatedTvSeries(page)
        return dto.results.map { it.toDomain() }
    }
}