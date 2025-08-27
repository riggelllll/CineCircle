package com.koniukhov.cinecircle.core.domain.model

data class MovieCollection(
    val id: Int,
    val name: String,
    val posterPath: String,
    val backdropPath: String
)