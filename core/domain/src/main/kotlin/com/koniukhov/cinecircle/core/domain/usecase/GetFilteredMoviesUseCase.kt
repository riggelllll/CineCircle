package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import javax.inject.Inject

class GetFilteredMoviesUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend operator fun invoke(
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
        withGenres: String? = null,
        withoutGenres: String? = null
    ) = repository.getFilteredMovies(
        page = page,
        language = language,
        sortBy = sortBy,
        year = year,
        releaseDateGte = releaseDateGte,
        releaseDateLte = releaseDateLte,
        genreId = genreId,
        minVoteAverage = minVoteAverage,
        maxVoteAverage = maxVoteAverage,
        minVoteCount = minVoteCount,
        maxVoteCount = maxVoteCount,
        withOriginCountry = withOriginCountry,
        withOriginalLanguage = withOriginalLanguage,
        withGenres = withGenres,
        withoutGenres = withoutGenres
    )
}