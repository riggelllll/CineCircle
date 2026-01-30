package com.koniukhov.cinecirclex.core.domain.model

data class MovieCollection(
    val id: Int,
    val name: String,
    val posterPath: String,
    val backdropPath: String
){
    companion object {
        fun empty() = MovieCollection(
            id = 0,
            name = "",
            posterPath = "",
            backdropPath = ""
        )
    }
}