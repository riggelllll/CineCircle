package com.koniukhov.cinecirclex.core.domain.model

data class Creator(
    val id: Int,
    val creditId: String,
    val name: String,
    val gender: Int,
    val profilePath: String
)