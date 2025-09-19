package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.MovieDetails

interface MoviesRepository {
    suspend fun getTrendingMovies(page: Int = 1, language: String): List<Movie>
    suspend fun getNowPlayingMovies(page: Int = 1, language: String): List<Movie>
    suspend fun getPopularMovies(page: Int = 1, language: String): List<Movie>
    suspend fun getTopRatedMovies(page: Int = 1, language: String): List<Movie>
    suspend fun getUpcomingMovies(page: Int = 1, language: String): List<Movie>
    suspend fun getMoviesByGenre(genreId: Int, page: Int = 1, language: String): List<Movie>
    suspend fun getMovieDetails(movieId: Int, language: String): MovieDetails
    suspend fun getMovieRecommendations(movieId: Int, page: Int = 1, language: String): List<Movie>
    suspend fun getSimilarMovies(movieId: Int, page: Int = 1, language: String): List<Movie>
    suspend fun getSearchedMovies(query: String, page: Int = 1, language: String): List<Movie>
    suspend fun getFilteredMovies(
        page: Int = 1,
        language: String,
        sortBy: String,
        year: Int? = null,
        releaseDateGte: String? = null,
        releaseDateLte: String? = null,
        genreId: String? = null,
        minVoteAverage: Float? = null,
        maxVoteAverage: Float? = null,
        minVoteCount: Int? = null,
        maxVoteCount: Int? = null,
        withOriginCountry: String? = null,
        withOriginalLanguage: String? = null,
        withoutGenres: String? = null
    ): List<Movie>
}