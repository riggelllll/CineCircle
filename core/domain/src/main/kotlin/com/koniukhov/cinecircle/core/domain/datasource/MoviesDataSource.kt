package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.MovieDetailsDto
import com.koniukhov.cinecircle.core.network.model.MoviesResponseDto

interface MoviesDataSource {
    suspend fun getTrendingMovies(page: Int, language: String): MoviesResponseDto
    suspend fun getNowPlayingMovies(page: Int, language: String): MoviesResponseDto
    suspend fun getPopularMovies(page: Int, language: String): MoviesResponseDto
    suspend fun getTopRatedMovies(page: Int, language: String): MoviesResponseDto
    suspend fun getUpcomingMovies(page: Int, language: String): MoviesResponseDto
    suspend fun getMoviesByGenre(genreId: Int, page: Int, language: String): MoviesResponseDto
    suspend fun getMovieDetails(movieId: Int, language: String): MovieDetailsDto
    suspend fun getMovieRecommendations(movieId: Int, page: Int, language: String): MoviesResponseDto
}