package com.koniukhov.cinecircle.core.network.api

import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.Movies
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.QueryParams.API_KEY
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.QueryParams.COLLECTION_ID
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.QueryParams.MOVIE_ID
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.QueryParams.TV_SEASON_NUMBER
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.QueryParams.TV_SERIES_ID
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.TVSeries
import com.koniukhov.cinecircle.core.network.model.CollectionDetailsDto
import com.koniukhov.cinecircle.core.network.model.ContentRatingsResponseDto
import com.koniukhov.cinecircle.core.network.model.GenreResponseDto
import com.koniukhov.cinecircle.core.network.model.MediaImagesDto
import com.koniukhov.cinecircle.core.network.model.MediaCreditsDto
import com.koniukhov.cinecircle.core.network.model.MovieDetailsDto
import com.koniukhov.cinecircle.core.network.model.MovieReviewsResponseDto
import com.koniukhov.cinecircle.core.network.model.MovieVideosDto
import com.koniukhov.cinecircle.core.network.model.MoviesResponseDto
import com.koniukhov.cinecircle.core.network.model.ReleaseDatesResponseDto
import com.koniukhov.cinecircle.core.network.model.TvSeasonDetailsDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesDetailsDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {

    @GET(Movies.TRENDING)
    suspend fun getTrendingMovies(
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MoviesResponseDto>

    @GET(Movies.NOW_PLAYING)
    suspend fun getNowPlayingMovies(
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MoviesResponseDto>

    @GET(Movies.POPULAR)
    suspend fun getPopularMovies(
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MoviesResponseDto>

    @GET(Movies.TOP_RATED)
    suspend fun getTopRatedMovies(
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MoviesResponseDto>

    @GET(Movies.UPCOMING)
    suspend fun getUpcomingMovies(
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MoviesResponseDto>

    @GET(TVSeries.AIRING_TODAY)
    suspend fun getAiringTodayTvSeries(
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<TvSeriesResponseDto>

    @GET(TVSeries.ON_THE_AIR)
    suspend fun getOnTheAirTvSeries(
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<TvSeriesResponseDto>

    @GET(TVSeries.TRENDING)
    suspend fun getTrendingTvSeries(
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<TvSeriesResponseDto>

    @GET(TVSeries.POPULAR)
    suspend fun getPopularTvSeries(
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<TvSeriesResponseDto>

    @GET(TVSeries.TOP_RATED)
    suspend fun getTopRatedTvSeries(
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<TvSeriesResponseDto>

    @GET(Movies.GENRES)
    suspend fun getMovieGenres(
        @Query(API_KEY) apiKey: String,
        @Query("language") language: String
    ): Response<GenreResponseDto>

    @GET(TVSeries.GENRES)
    suspend fun getTvSeriesGenres(
        @Query(API_KEY) apiKey: String,
        @Query("language") language: String
    ): Response<GenreResponseDto>

    @GET(Movies.DISCOVER)
    suspend fun getMoviesByGenre(
        @Query(API_KEY) apiKey: String,
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MoviesResponseDto>

    @GET(TVSeries.DISCOVER)
    suspend fun getTvSeriesByGenre(
        @Query(API_KEY) apiKey: String,
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<TvSeriesResponseDto>

    @GET(Movies.DETAILS)
    suspend fun getMovieDetails(
        @Path(MOVIE_ID) movieId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("language") language: String
    ): Response<MovieDetailsDto>

    @GET(Movies.COLLECTION_DETAILS)
    suspend fun getCollectionDetails(
        @Path(COLLECTION_ID) collectionId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("language") language: String
    ): Response<CollectionDetailsDto>

    @GET(Movies.IMAGES)
    suspend fun getMovieImages(
        @Path(MOVIE_ID) movieId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("language") language: String
    ): Response<MediaImagesDto>

    @GET(Movies.VIDEOS)
    suspend fun getMovieVideos(
        @Path(MOVIE_ID) movieId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("language") language: String
    ): Response<MovieVideosDto>

    @GET(Movies.REVIEWS)
    suspend fun getMovieReviews(
        @Path(MOVIE_ID) movieId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MovieReviewsResponseDto>

    @GET(Movies.CREDITS)
    suspend fun getMovieCredits(
        @Path(MOVIE_ID) movieId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("language") language: String
    ): Response<MediaCreditsDto>

    @GET(Movies.RECOMMENDATIONS)
    suspend fun getMovieRecommendations(
        @Path(MOVIE_ID) movieId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MoviesResponseDto>

    @GET(Movies.SIMILAR)
    suspend fun getSimilarMovies(
        @Path(MOVIE_ID) movieId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MoviesResponseDto>

    @GET(Movies.RELEASE_DATES)
    suspend fun getMovieReleaseDates(
        @Path(MOVIE_ID) movieId: Int,
        @Query(API_KEY) apiKey: String
    ): Response<ReleaseDatesResponseDto>

    @GET(TVSeries.DETAILS)
    suspend fun getTvSeriesDetails(
        @Path(TV_SERIES_ID) tvSeriesId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("language") language: String
    ): Response<TvSeriesDetailsDto>

    @GET(TVSeries.SEASON_DETAILS)
    suspend fun getTvSeasonDetails(
        @Path(TV_SERIES_ID) tvSeriesId: Int,
        @Path(TV_SEASON_NUMBER) seasonNumber: Int,
        @Query(API_KEY) apiKey: String,
        @Query("language") language: String
    ): Response<TvSeasonDetailsDto>

    @GET(TVSeries.IMAGES)
    suspend fun getTvSeriesImages(
        @Path(TV_SERIES_ID) tvSeriesId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("language") language: String
    ): Response<MediaImagesDto>

    @GET(TVSeries.VIDEOS)
    suspend fun getTvSeriesVideos(
        @Path(TV_SERIES_ID) tvSeriesId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("language") language: String
    ): Response<MovieVideosDto>

    @GET(TVSeries.REVIEWS)
    suspend fun getTvSeriesReviews(
        @Path(TV_SERIES_ID) tvSeriesId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MovieReviewsResponseDto>

    @GET(TVSeries.CREDITS)
    suspend fun getTvSeriesCredits(
        @Path(TV_SERIES_ID) tvSeriesId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("language") language: String
    ): Response<MediaCreditsDto>

    @GET(TVSeries.CONTENT_RATINGS)
    suspend fun getTvSeriesContentRatings(
        @Path(TV_SERIES_ID) tvSeriesId: Int,
        @Query(API_KEY) apiKey: String
    ): Response<ContentRatingsResponseDto>

    @GET(TVSeries.RECOMMENDATIONS)
    suspend fun getTvSeriesRecommendations(
        @Path(TV_SERIES_ID) tvSeriesId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<TvSeriesResponseDto>

    @GET(TVSeries.SIMILAR)
    suspend fun getSimilarTvSeries(
        @Path(TV_SERIES_ID) tvSeriesId: Int,
        @Query(API_KEY) apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<TvSeriesResponseDto>

    @GET(Movies.SEARCH)
    suspend fun getSearchedMovies(
        @Query(API_KEY) apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("language") language: String,
    ): Response<MoviesResponseDto>

    @GET(TVSeries.SEARCH)
    suspend fun getSearchedTvSeries(
        @Query(API_KEY) apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("language") language: String,
    ): Response<TvSeriesResponseDto>
}