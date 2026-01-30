package com.koniukhov.cinecirclex.core.network.model

import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID

data class MediaImagesDto(
    val id: Int?,
    val backdrops: List<ImageDto>?,
    val logos: List<ImageDto>?,
    val posters: List<ImageDto>?
){
    companion object{
        fun empty() = MediaImagesDto(INVALID_ID, emptyList(), emptyList(), emptyList())
    }
}