package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.MoviesResponseDto

interface MoviesDataSource {
    suspend fun getPopularMovies(page: Int): MoviesResponseDto
}