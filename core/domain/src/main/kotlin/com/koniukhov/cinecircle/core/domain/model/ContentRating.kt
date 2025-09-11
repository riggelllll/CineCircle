package com.koniukhov.cinecircle.core.domain.model

data class ContentRating(
    val descriptors: List<String>,
    val languageCode: String,
    val rating: String
)