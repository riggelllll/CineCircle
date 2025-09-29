package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.network.model.MovieDetailsDto
import com.koniukhov.cinecircle.core.network.model.MoviesResponseDto

interface RemoteMoviesDataSource {
    suspend fun getTrendingMovies(page: Int, language: String): MoviesResponseDto
    suspend fun getNowPlayingMovies(page: Int, language: String): MoviesResponseDto
    suspend fun getPopularMovies(page: Int, language: String): MoviesResponseDto
    suspend fun getTopRatedMovies(page: Int, language: String): MoviesResponseDto
    suspend fun getUpcomingMovies(page: Int, language: String): MoviesResponseDto
    suspend fun getMoviesByGenre(genreId: Int, page: Int, language: String): MoviesResponseDto
    suspend fun getMovieDetails(movieId: Int, language: String): MovieDetailsDto
    suspend fun getMovieRecommendations(movieId: Int, page: Int, language: String): MoviesResponseDto
    suspend fun getSimilarMovies(movieId: Int, page: Int, language: String): MoviesResponseDto
    suspend fun getSearchedMovies(query: String, page: Int, language: String): MoviesResponseDto
    suspend fun getFilteredMovies(
        page: Int,
        language: String,
        sortBy: String,
        year: Int?,
        releaseDateGte: String?,
        releaseDateLte: String?,
        minVoteAverage: Float?,
        maxVoteAverage: Float?,
        minVoteCount: Int?,
        maxVoteCount: Int?,
        withOriginCountry: String?,
        withOriginalLanguage: String?,
        withGenres: String?,
        withoutGenres: String?
    ): MoviesResponseDto
}