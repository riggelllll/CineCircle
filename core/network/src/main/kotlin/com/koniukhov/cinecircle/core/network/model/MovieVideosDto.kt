package com.koniukhov.cinecircle.core.network.model

data class MovieVideosDto (
    val id: Int,
    val results: List<VideoDto>
){
    companion object{
        fun empty() = MovieVideosDto(
            id = 0,
            results = emptyList()
        )
    }
}