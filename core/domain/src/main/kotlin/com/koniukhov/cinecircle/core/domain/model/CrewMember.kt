package com.koniukhov.cinecircle.core.domain.model

data class CrewMember(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Float,
    val profilePath: String,
    val creditId: String,
    val department: String,
    val job: String
)