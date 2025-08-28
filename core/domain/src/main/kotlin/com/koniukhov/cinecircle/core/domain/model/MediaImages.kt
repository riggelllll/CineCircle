package com.koniukhov.cinecircle.core.domain.model

data class MediaImages(
    val id: Int,
    val backdrops: List<Image>,
    val logos: List<Image>,
    val posters: List<Image>
)