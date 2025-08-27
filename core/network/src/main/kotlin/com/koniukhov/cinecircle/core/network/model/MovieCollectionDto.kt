package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName

data class MovieCollectionDto(
    val id: Int,
    val name: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("backdrop_path")
    val backdropPath: String
){
    companion object {
        fun empty() = MovieCollectionDto(
            id = -1,
            name = "",
            posterPath = "",
            backdropPath = ""
        )
    }
}