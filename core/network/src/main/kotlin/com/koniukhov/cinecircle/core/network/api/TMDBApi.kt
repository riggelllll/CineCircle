package com.koniukhov.cinecircle.core.network.api

import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.API_KEY
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.MOVIE_POPULAR
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.NOW_PLAYING
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.TOP_RATED
import com.koniukhov.cinecircle.core.network.model.MoviesResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {
    @GET(NOW_PLAYING)
    suspend fun getNowPlayingMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<MoviesResponseDto>

    @GET(MOVIE_POPULAR)
    suspend fun getPopularMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<MoviesResponseDto>

    @GET(TOP_RATED)
    suspend fun getTopRatedMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<MoviesResponseDto>
}