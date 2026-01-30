package com.koniukhov.cinecirclex.core.domain.model

data class CollectionMedia(
    val adult: Boolean,
    val backdropPath: String,
    val id: Int,
    val title: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String,
    val mediaType: String,
    val genreIds: List<Int>,
    val popularity: Float,
    val releaseDate: String,
    val video: Boolean,
    val voteAverage: Float,
    val voteCount: Int
)
