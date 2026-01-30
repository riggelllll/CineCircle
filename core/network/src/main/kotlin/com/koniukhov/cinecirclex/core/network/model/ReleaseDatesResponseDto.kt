package com.koniukhov.cinecirclex.core.network.model

import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID

data class ReleaseDatesResponseDto (
    val id: Int?,
    val results: List<ReleaseDatesResultDto>?
){
    companion object{
        fun empty() = ReleaseDatesResponseDto(INVALID_ID, emptyList())
    }
}