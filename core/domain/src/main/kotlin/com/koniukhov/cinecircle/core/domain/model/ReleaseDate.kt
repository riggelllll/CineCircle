package com.koniukhov.cinecircle.core.domain.model

data class ReleaseDate(
    val certification: String?,
    val descriptors: List<String>?,
    val languageCode: String?,
    val note: String?,
    val releaseDate: String?,
    val releaseType: Int?
)