package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.domain.datasource.MoviesDataSource
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.MovieDetails
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: MoviesDataSource
) : MoviesRepository {

    override suspend fun getTrendingMovies(page: Int, language: String): List<Movie> {
        val dto = remoteDataSource.getTrendingMovies(page, language)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getNowPlayingMovies(page: Int, language: String): List<Movie> {
        val dto = remoteDataSource.getNowPlayingMovies(page, language)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getPopularMovies(page: Int, language: String): List<Movie> {
        val dto = remoteDataSource.getPopularMovies(page, language)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getTopRatedMovies(page: Int, language: String): List<Movie> {
        val dto = remoteDataSource.getTopRatedMovies(page, language)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getUpcomingMovies(page: Int, language: String): List<Movie> {
        val dto = remoteDataSource.getUpcomingMovies(page, language)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getMoviesByGenre(
        genreId: Int,
        page: Int,
        language: String
    ): List<Movie> {
        val dto = remoteDataSource.getMoviesByGenre(genreId, page, language)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getMovieDetails(
        movieId: Int,
        language: String
    ): MovieDetails {
        val dto = remoteDataSource.getMovieDetails(movieId, language)
        return dto.toDomain()
    }

    override suspend fun getMovieRecommendations(
        movieId: Int,
        page: Int,
        language: String
    ): List<Movie> {
        val dto = remoteDataSource.getMovieRecommendations(movieId, page, language)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getSimilarMovies(
        movieId: Int,
        page: Int,
        language: String
    ): List<Movie> {
        val dto = remoteDataSource.getSimilarMovies(movieId, page, language)
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getSearchedMovies(
        query: String,
        page: Int,
        language: String
    ): List<Movie> {
        val dto = remoteDataSource.getSearchedMovies(query, page, language)
        return dto.results.map { it.toDomain() }
    }
}