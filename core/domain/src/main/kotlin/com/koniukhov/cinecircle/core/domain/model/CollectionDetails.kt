package com.koniukhov.cinecircle.core.domain.model

import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID

data class CollectionDetails(
    val id: Int,
    val name: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val parts: List<CollectionMedia>
){
    fun exists() = id != INVALID_ID
}