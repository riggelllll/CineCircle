package com.koniukhov.cinecirclex.core.network.model

import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID

data class MediaCreditsDto(
    val id: Int?,
    val cast: List<CastMemberDto>?,
    val crew: List<CrewMemberDto>?
){
    companion object{
        fun empty() = MediaCreditsDto(
            id = INVALID_ID,
            cast = emptyList(),
            crew = emptyList()
        )
    }
}