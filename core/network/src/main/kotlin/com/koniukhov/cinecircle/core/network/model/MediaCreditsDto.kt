package com.koniukhov.cinecircle.core.network.model

data class MediaCreditsDto(
    val id: Int,
    val cast: List<CastMemberDto>,
    val crew: List<CrewMemberDto>
){
    companion object{
        fun empty() = MediaCreditsDto(
            id = 0,
            cast = emptyList(),
            crew = emptyList()
        )
    }
}