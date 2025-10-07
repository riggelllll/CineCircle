package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.local.LocalMoviesDataSource
import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.mapper.toMovieDetails
import com.koniukhov.cinecircle.core.data.remote.RemoteMoviesDataSource
import com.koniukhov.cinecircle.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.MovieDetails
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteMoviesDataSource,
    private val localDataSource: LocalMoviesDataSource,
    private val networkStatusProvider: NetworkStatusProvider
) : MoviesRepository {

    override suspend fun getTrendingMovies(page: Int, language: String): List<Movie> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getTrendingMovies(page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getNowPlayingMovies(page: Int, language: String): List<Movie> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getNowPlayingMovies(page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getPopularMovies(page: Int, language: String): List<Movie> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getPopularMovies(page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getTopRatedMovies(page: Int, language: String): List<Movie> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getTopRatedMovies(page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getUpcomingMovies(page: Int, language: String): List<Movie> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getUpcomingMovies(page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getMoviesByGenre(
        genreId: Int,
        page: Int,
        language: String
    ): List<Movie> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getMoviesByGenre(genreId, page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getMovieDetails(
        movieId: Int,
        language: String
    ): MovieDetails {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getMovieDetails(movieId, language)
                dto.toDomain() },
            localCall = { localDataSource.getMovieWithGenres(movieId)?.toMovieDetails()},
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: MovieDetails.empty()
    }

    override suspend fun getMovieRecommendations(
        movieId: Int,
        page: Int,
        language: String
    ): List<Movie> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getMovieRecommendations(movieId, page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { emptyList() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getSimilarMovies(
        movieId: Int,
        page: Int,
        language: String
    ): List<Movie> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getSimilarMovies(movieId, page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { emptyList() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getSearchedMovies(
        query: String,
        page: Int,
        language: String
    ): List<Movie> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getSearchedMovies(query, page, language)
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getFilteredMovies(
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
    ): List<Movie> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteDataSource.getFilteredMovies(
                    page = page,
                    language = language,
                    sortBy = sortBy,
                    year = year,
                    releaseDateGte = releaseDateGte,
                    releaseDateLte = releaseDateLte,
                    minVoteAverage = minVoteAverage,
                    maxVoteAverage = maxVoteAverage,
                    minVoteCount = minVoteCount,
                    maxVoteCount = maxVoteCount,
                    withOriginCountry = withOriginCountry,
                    withOriginalLanguage = withOriginalLanguage,
                    withGenres = withGenres,
                    withoutGenres = withoutGenres
                )
                dto.results?.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }
}