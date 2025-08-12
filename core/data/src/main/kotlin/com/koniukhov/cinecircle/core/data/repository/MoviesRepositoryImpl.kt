package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.domain.datasource.MoviesDataSource
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: MoviesDataSource
) : MoviesRepository {
    override suspend fun getPopularMovies(page: Int): List<Movie> {
        val dto = remoteDataSource.getPopularMovies(page)
        return dto.results.map { it.toDomain() }.toList()
    }
}