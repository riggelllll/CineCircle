package com.koniukhov.cinecircle.core.domain.model

data class ReleaseDateResult(
    val countryCode: String,
    val releaseDates: List<ReleaseDate>
)