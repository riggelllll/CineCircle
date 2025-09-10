package com.koniukhov.cinecircle.core.domain.model

data class MovieDetails(
    val adult: Boolean,
    val backdropPath: String,
    val belongsToCollection: MovieCollection,
    val budget: Int,
    val genres: List<Genre>,
    val homePage: String,
    val id: Int,
    val imdbId: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Float,
    val posterPath: String,
    val productionCompanies: List<ProductionCompany>,
    val productionCountries: List<ProductionCountry>,
    val releaseDate: String,
    val revenue: Long,
    val runtime: Int,
    val spokenLanguages: List<Language>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Float,
    val voteCount: Int
)