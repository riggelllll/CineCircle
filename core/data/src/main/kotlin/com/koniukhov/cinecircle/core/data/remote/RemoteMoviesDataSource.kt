package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.domain.datasource.MoviesDataSource
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.MoviesResponseDto
import javax.inject.Inject

class RemoteMoviesDataSource @Inject constructor(
    private val api: TMDBApi
) : MoviesDataSource{
    override suspend fun getNowPlayingMovies(page: Int): MoviesResponseDto {
        return api.getNowPlayingMovies(BuildConfig.API_KEY, page).body() ?: MoviesResponseDto.empty()
    }
    override suspend fun getPopularMovies(page: Int): MoviesResponseDto {
        return api.getPopularMovies(BuildConfig.API_KEY, page).body() ?: MoviesResponseDto.empty()
    }

    override suspend fun getTopRatedMovies(page: Int): MoviesResponseDto {
        return api.getTopRatedMovies(BuildConfig.API_KEY, page).body() ?: MoviesResponseDto.empty()
    }
}