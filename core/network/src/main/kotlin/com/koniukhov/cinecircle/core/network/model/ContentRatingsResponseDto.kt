package com.koniukhov.cinecircle.core.network.model

import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID

data class ContentRatingsResponseDto(
    val id: Int?,
    val results: List<ContentRatingDto>?
){
    companion object{
        fun empty() = ContentRatingsResponseDto(INVALID_ID, emptyList())
    }
}