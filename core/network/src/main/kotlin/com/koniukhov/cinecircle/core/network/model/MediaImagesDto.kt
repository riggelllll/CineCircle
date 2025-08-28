package com.koniukhov.cinecircle.core.network.model

data class MediaImagesDto(
    val id: Int,
    val backdrops: List<ImageDto>,
    val logos: List<ImageDto>,
    val posters: List<ImageDto>
){
    companion object{
        fun empty() = MediaImagesDto(-1, emptyList(), emptyList(), emptyList())
    }
}