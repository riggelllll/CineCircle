package com.koniukhov.cinecirclex.core.domain.model

import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID

data class CollectionDetails(
    val id: Int,
    val name: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val parts: List<CollectionMedia>
){
    fun exists() = id != INVALID_ID

    companion object {
        fun empty() = CollectionDetails(
            id = INVALID_ID,
            name = "",
            overview = "",
            posterPath = "",
            backdropPath = "",
            parts = emptyList()
        )
    }
}