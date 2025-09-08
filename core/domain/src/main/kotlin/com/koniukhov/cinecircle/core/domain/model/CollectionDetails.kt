package com.koniukhov.cinecircle.core.domain.model

data class CollectionDetails(
    val id: Int,
    val name: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val parts: List<CollectionMedia>
){
    fun exists() = id != -1
}