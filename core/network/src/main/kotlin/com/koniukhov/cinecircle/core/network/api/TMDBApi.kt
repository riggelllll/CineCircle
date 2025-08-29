package com.koniukhov.cinecircle.core.network.api

import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.AIRING_TODAY_TV_SERIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.API_KEY
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.COLLECTION_DETAILS
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.COLLECTION_ID
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.DISCOVER_MOVIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.DISCOVER_TV_SERIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.MOVIE_DETAILS
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.MOVIE_GENRES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.MOVIE_ID
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.MOVIE_IMAGES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.MOVIE_REVIEWS
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.MOVIE_VIDEOS
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.NOW_PLAYING_MOVIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.ON_THE_AIR_TV_SERIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.POPULAR_MOVIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.POPULAR_TV_SERIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.TOP_RATED_MOVIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.TOP_RATED_TV_SERIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.TRENDING_MOVIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.TRENDING_TV_SERIES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.TV_SERIES_GENRES
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.UPCOMING_MOVIES
import com.koniukhov.cinecircle.core.network.model.CollectionDetailsDto
import com.koniukhov.cinecircle.core.network.model.GenreResponseDto
import com.koniukhov.cinecircle.core.network.model.MovieDetailsDto
import com.koniukhov.cinecircle.core.network.model.MediaImagesDto
import com.koniukhov.cinecircle.core.network.model.MovieReviewsResponseDto
import com.koniukhov.cinecircle.core.network.model.MovieVideosDto
import com.koniukhov.cinecircle.core.network.model.MoviesResponseDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {
    @GET(TRENDING_MOVIES)
    suspend fun getTrendingMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int, @Query("language") language: String): Response<MoviesResponseDto>
    @GET(NOW_PLAYING_MOVIES)
    suspend fun getNowPlayingMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int, @Query("language") language: String): Response<MoviesResponseDto>

    @GET(POPULAR_MOVIES)
    suspend fun getPopularMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int, @Query("language") language: String): Response<MoviesResponseDto>

    @GET(TOP_RATED_MOVIES)
    suspend fun getTopRatedMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int, @Query("language") language: String): Response<MoviesResponseDto>

    @GET(UPCOMING_MOVIES)
    suspend fun getUpcomingMovies(@Query(API_KEY) apiKey: String, @Query("page") page: Int, @Query("language") language: String): Response<MoviesResponseDto>

    @GET(AIRING_TODAY_TV_SERIES)
    suspend fun getAiringTodayTvSeries(@Query(API_KEY) apiKey: String, @Query("page") page: Int, @Query("language") language: String): Response<TvSeriesResponseDto>

    @GET(ON_THE_AIR_TV_SERIES)
    suspend fun getOnTheAirTvSeries(@Query(API_KEY) apiKey: String, @Query("page") page: Int, @Query("language") language: String): Response<TvSeriesResponseDto>

    @GET(TRENDING_TV_SERIES)
    suspend fun getTrendingTvSeries(@Query(API_KEY) apiKey: String, @Query("page") page: Int, @Query("language") language: String): Response<TvSeriesResponseDto>

    @GET(POPULAR_TV_SERIES)
    suspend fun getPopularTvSeries(@Query(API_KEY) apiKey: String, @Query("page") page: Int, @Query("language") language: String): Response<TvSeriesResponseDto>

    @GET(TOP_RATED_TV_SERIES)
    suspend fun getTopRatedTvSeries(@Query(API_KEY) apiKey: String, @Query("page") page: Int, @Query("language") language: String): Response<TvSeriesResponseDto>

    @GET(MOVIE_GENRES)
    suspend fun getMovieGenres(@Query(API_KEY) apiKey: String, @Query("language") language: String): Response<GenreResponseDto>

    @GET(TV_SERIES_GENRES)
    suspend fun getTvSeriesGenres(@Query(API_KEY) apiKey: String, @Query("language") language: String): Response<GenreResponseDto>

    @GET(DISCOVER_MOVIES)
    suspend fun getMoviesByGenre(
        @Query(API_KEY) apiKey: String,
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MoviesResponseDto>

    @GET(DISCOVER_TV_SERIES)
    suspend fun getTvSeriesByGenre(
        @Query(API_KEY) apiKey: String,
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<TvSeriesResponseDto>

    @GET(MOVIE_DETAILS)
    suspend fun getMovieDetails(@Path(MOVIE_ID) movieId: Int, @Query(API_KEY) apiKey: String, @Query("language") language: String): Response<MovieDetailsDto>

    @GET(COLLECTION_DETAILS)
    suspend fun getCollectionDetails(@Path(COLLECTION_ID) collectionId: Int, @Query(API_KEY) apiKey: String, @Query("language") language: String): Response<CollectionDetailsDto>

    @GET(MOVIE_IMAGES)
    suspend fun getMovieImages(@Path(MOVIE_ID) movieId: Int, @Query(API_KEY) apiKey: String, @Query("language") language: String): Response<MediaImagesDto>

    @GET(MOVIE_VIDEOS)
    suspend fun getMovieVideos(@Path(MOVIE_ID) movieId: Int, @Query(API_KEY) apiKey: String, @Query("language") language: String): Response<MovieVideosDto>

    @GET(MOVIE_REVIEWS)
    suspend fun getMovieReviews(@Path(MOVIE_ID) movieId: Int, @Query(API_KEY) apiKey: String, @Query("page") page: Int, @Query("language") language: String): Response<MovieReviewsResponseDto>
}