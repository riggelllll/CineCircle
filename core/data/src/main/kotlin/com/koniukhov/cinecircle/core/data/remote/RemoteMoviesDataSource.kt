package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.domain.datasource.MoviesDataSource
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.MoviesResponseDto
import javax.inject.Inject

class RemoteMoviesDataSource @Inject constructor(
    private val api: TMDBApi
) : MoviesDataSource{

    override suspend fun getTrendingMovies(page: Int, language: String): MoviesResponseDto {
        return api.getTrendingMovies(BuildConfig.API_KEY, page, language).body() ?: MoviesResponseDto.empty()
    }

    override suspend fun getNowPlayingMovies(page: Int, language: String): MoviesResponseDto {
        return api.getNowPlayingMovies(BuildConfig.API_KEY, page, language).body() ?: MoviesResponseDto.empty()
    }

    override suspend fun getPopularMovies(page: Int, language: String): MoviesResponseDto {
        return api.getPopularMovies(BuildConfig.API_KEY, page, language).body() ?: MoviesResponseDto.empty()
    }

    override suspend fun getTopRatedMovies(page: Int, language: String): MoviesResponseDto {
        return api.getTopRatedMovies(BuildConfig.API_KEY, page, language).body() ?: MoviesResponseDto.empty()
    }

    override suspend fun getUpcomingMovies(page: Int, language: String): MoviesResponseDto {
        return api.getUpcomingMovies(BuildConfig.API_KEY, page, language).body() ?: MoviesResponseDto.empty()
    }

    override suspend fun getMoviesByGenre(
        genreId: Int,
        page: Int,
        language: String
    ): MoviesResponseDto {
        return api.getMoviesByGenre(BuildConfig.API_KEY, genreId, page, language).body() ?: MoviesResponseDto.empty()
    }
}