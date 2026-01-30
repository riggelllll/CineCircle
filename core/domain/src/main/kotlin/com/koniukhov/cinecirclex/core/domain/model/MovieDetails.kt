package com.koniukhov.cinecirclex.core.domain.model

import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID

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
) {
    companion object {
        fun empty() = MovieDetails(
            adult = false,
            backdropPath = "",
            belongsToCollection = MovieCollection.empty(),
            budget = 0,
            genres = emptyList(),
            homePage = "",
            id = INVALID_ID,
            imdbId = "",
            originalLanguage = "",
            originalTitle = "",
            overview = "",
            popularity = 0f,
            posterPath = "",
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            releaseDate = "",
            revenue = 0L,
            runtime = 0,
            spokenLanguages = emptyList(),
            status = "",
            tagline = "",
            title = "",
            video = false,
            voteAverage = 0f,
            voteCount = 0
        )
    }
}