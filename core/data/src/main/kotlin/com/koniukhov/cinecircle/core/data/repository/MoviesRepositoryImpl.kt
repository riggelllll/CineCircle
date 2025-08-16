package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.domain.datasource.MoviesDataSource
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: MoviesDataSource
) : MoviesRepository {

    override suspend fun getTrendingMovies(page: Int): List<Movie> {
        val dto = remoteDataSource.getTrendingMovies(page)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getNowPlayingMovies(page: Int): List<Movie> {
        val dto = remoteDataSource.getNowPlayingMovies(page)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getPopularMovies(page: Int): List<Movie> {
        val dto = remoteDataSource.getPopularMovies(page)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getTopRatedMovies(page: Int): List<Movie> {
        val dto = remoteDataSource.getTopRatedMovies(page)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getUpcomingMovies(page: Int): List<Movie> {
        val dto = remoteDataSource.getUpcomingMovies(page)
        return dto.results.map { it.toDomain() }
    }
}