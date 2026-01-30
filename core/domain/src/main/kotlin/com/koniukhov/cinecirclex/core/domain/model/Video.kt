package com.koniukhov.cinecirclex.core.domain.model

data class Video(
    val languageCode: String,
    val countryCode: String,
    val name: String,
    val key: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    val publishedAt: String,
    val id: String
)