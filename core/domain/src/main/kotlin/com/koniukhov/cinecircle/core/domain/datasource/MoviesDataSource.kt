package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.MoviesResponseDto

interface MoviesDataSource {
    suspend fun getTrendingMovies(page: Int): MoviesResponseDto
    suspend fun getNowPlayingMovies(page: Int): MoviesResponseDto
    suspend fun getPopularMovies(page: Int): MoviesResponseDto
    suspend fun getTopRatedMovies(page: Int): MoviesResponseDto
    suspend fun getUpcomingMovies(page: Int): MoviesResponseDto
}