package com.koniukhov.cinecirclex.core.domain.model

data class ContentRating(
    val descriptors: List<String>,
    val countryCode: String,
    val rating: String
)