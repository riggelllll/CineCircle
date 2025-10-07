package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName
import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID

data class CollectionDetailsDto(
    val id: Int?,
    val name: String?,
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val parts: List<CollectionMediaDto>?
){
    companion object{
        fun empty() = CollectionDetailsDto(
            id = INVALID_ID,
            name = "",
            overview = "",
            posterPath = "",
            backdropPath = "",
            parts = emptyList()
        )
    }
}