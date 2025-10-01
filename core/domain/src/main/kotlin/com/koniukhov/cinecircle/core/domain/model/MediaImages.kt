package com.koniukhov.cinecircle.core.domain.model

import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID

data class MediaImages(
    val id: Int,
    val backdrops: List<Image>,
    val logos: List<Image>,
    val posters: List<Image>
) {
    companion object {
        fun empty() = MediaImages(
            id = INVALID_ID,
            backdrops = emptyList(),
            logos = emptyList(),
            posters = emptyList()
        )
    }
}