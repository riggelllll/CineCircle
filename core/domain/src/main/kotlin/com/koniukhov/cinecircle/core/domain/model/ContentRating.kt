package com.koniukhov.cinecircle.core.domain.model

data class ContentRating(
    val descriptors: List<String>,
    val countryCode: String,
    val rating: String
)