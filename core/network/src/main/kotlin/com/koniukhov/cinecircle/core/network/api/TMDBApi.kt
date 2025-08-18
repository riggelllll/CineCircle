package com.koniukhov.cinecircle.core.network.api

import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.AIRING_TODAY_TV_SERIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.API_KEY
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.NOW_PLAYING_MOVIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.ON_THE_AIR_TV_SERIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.POPULAR_MOVIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.POPULAR_TV_SERIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.TOP_RATED_MOVIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.TOP_RATED_TV_SERIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.TRENDING_MOVIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.TRENDING_TV_SERIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.UPCOMING_MOVIES
import com.koniukhov.cinecircle.core.network.model.MoviesResponseDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {
    @GET(TRENDING_MOVIES)
    suspend fun getTrendingMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<MoviesResponseDto>
    @GET(NOW_PLAYING_MOVIES)
    suspend fun getNowPlayingMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<MoviesResponseDto>

    @GET(POPULAR_MOVIES)
    suspend fun getPopularMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<MoviesResponseDto>

    @GET(TOP_RATED_MOVIES)
    suspend fun getTopRatedMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<MoviesResponseDto>

    @GET(UPCOMING_MOVIES)
    suspend fun getUpcomingMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<MoviesResponseDto>

    @GET(AIRING_TODAY_TV_SERIES)
    suspend fun getAiringTodayTvSeries(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<TvSeriesResponseDto>

    @GET(ON_THE_AIR_TV_SERIES)
    suspend fun getOnTheAirTvSeries(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<TvSeriesResponseDto>

    @GET(TRENDING_TV_SERIES)
    suspend fun getTrendingTvSeries(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<TvSeriesResponseDto>

    @GET(POPULAR_TV_SERIES)
    suspend fun getPopularTvSeries(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<TvSeriesResponseDto>

    @GET(TOP_RATED_TV_SERIES)
    suspend fun getTopRatedTvSeries(@Query(API_KEY) apiKey: String, @Query("page") page: Int): Response<TvSeriesResponseDto>
}