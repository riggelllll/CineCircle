package com.koniukhov.cinecirclex.feature.search.model

data class TvSeriesFilterParams (
    val sortBy: String? = null,
    val airDateGte: String?,
    val airDateLte: String? = null,
    val year: Int? = null,
    val firstAirDateGte: String? = null,
    val firstAirDateLte: String? = null,
    val minVoteAverage: Float? = null,
    val maxVoteAverage: Float? = null,
    val minVoteCount: Int? = null,
    val maxVoteCount: Int? = null,
    val withOriginCountry: String? = null,
    val withOriginalLanguage: String? = null,
    val withGenres: String? = null,
    val withoutGenres: String? = null
)