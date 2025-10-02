package com.koniukhov.cinecircle.core.network.model

data class MediaVideosDto (
    val id: Int,
    val results: List<VideoDto>
){
    companion object{
        fun empty() = MediaVideosDto(
            id = 0,
            results = emptyList()
        )
    }
}