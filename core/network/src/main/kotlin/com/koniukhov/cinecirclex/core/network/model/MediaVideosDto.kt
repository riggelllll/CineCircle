package com.koniukhov.cinecirclex.core.network.model

import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID

data class MediaVideosDto (
    val id: Int?,
    val results: List<VideoDto>?
){
    companion object{
        fun empty() = MediaVideosDto(
            id = INVALID_ID,
            results = emptyList()
        )
    }
}