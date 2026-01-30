package com.koniukhov.cinecirclex.core.domain.model

data class Movie (
    val adult: Boolean,
    val backdropPath: String,
    val genreIds: List<Int>,
    override val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Float,
    override val posterPath: String,
    val releaseDate: String,
    override val title: String,
    val video: Boolean,
    override val voteAverage: Float,
    val voteCount: Int
) : MediaItem()