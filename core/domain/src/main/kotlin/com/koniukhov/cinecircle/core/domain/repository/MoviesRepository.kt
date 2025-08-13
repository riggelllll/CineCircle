package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.Movie

interface MoviesRepository {
    suspend fun getNowPlayingMovies(page: Int = 1): List<Movie>
    suspend fun getPopularMovies(page: Int = 1): List<Movie>
    suspend fun getTopRatedMovies(page: Int = 1): List<Movie>
}