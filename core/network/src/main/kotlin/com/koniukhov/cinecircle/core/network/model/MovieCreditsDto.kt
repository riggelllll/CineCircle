package com.koniukhov.cinecircle.core.network.model

data class MovieCreditsDto(
    val id: Int,
    val cast: List<CastMemberDto>,
    val crew: List<CrewMemberDto>
){
    companion object{
        fun empty() = MovieCreditsDto(
            id = 0,
            cast = emptyList(),
            crew = emptyList()
        )
    }
}