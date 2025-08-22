package com.koniukhov.cinecircle.core.domain.model

data class TvSeries(
    val adult: Boolean,
    val backdropPath: String,
    val genreIds: List<Int>,
    override val id: Int,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Float,
    val posterPath: String,
    val firstAirDate: String,
    val name: String,
    val voteAverage: Float,
    val voteCount: Int
) : MediaItem()