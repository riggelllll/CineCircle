package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import javax.inject.Inject

class GetFilteredTvSeriesUseCase @Inject constructor(private val repository: TvSeriesRepository) {
    suspend operator fun invoke(
        page: Int = 1,
        language: String,
        sortBy: String,
        airDateGte: String? = null,
        airDateLte: String? = null,
        year: Int? = null,
        firstAirDateGte: String? = null,
        firstAirDateLte: String? = null,
        minVoteAverage: Float? = null,
        maxVoteAverage: Float? = null,
        minVoteCount: Int? = null,
        maxVoteCount: Int? = null,
        withOriginCountry: String? = null,
        withOriginalLanguage: String? = null,
        withGenres: String? = null,
        withoutGenres: String? = null
    ) = repository.getFilteredTvSeries(
        page = page,
        language = language,
        sortBy = sortBy,
        airDateGte = airDateGte,
        airDateLte = airDateLte,
        year = year,
        firstAirDateGte = firstAirDateGte,
        firstAirDateLte = firstAirDateLte,
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