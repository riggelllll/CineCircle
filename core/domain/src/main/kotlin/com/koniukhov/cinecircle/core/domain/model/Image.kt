package com.koniukhov.cinecircle.core.domain.model

data class Image(
    val filePath: String,
    val width: Int,
    val height: Int,
    val countryCode: String,
    val aspectRatio: Double,
    val voteAverage: Double,
    val voteCount: Int
)