package com.koniukhov.cinecircle.core.domain.model

data class CastMember(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Float,
    val profilePath: String,
    val castId: Int,
    val character: String,
    val creditId: String,
    val order: Int
)