package com.koniukhov.cinecirclex.core.network.model

import com.google.gson.annotations.SerializedName
import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID

data class MovieCollectionDto(
    val id: Int?,
    val name: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?
){
    companion object {
        fun empty() = MovieCollectionDto(
            id = INVALID_ID,
            name = "",
            posterPath = "",
            backdropPath = ""
        )
    }
}