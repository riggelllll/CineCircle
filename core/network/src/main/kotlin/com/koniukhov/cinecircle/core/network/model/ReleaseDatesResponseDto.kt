package com.koniukhov.cinecircle.core.network.model

data class ReleaseDatesResponseDto (
    val id: Int,
    val results: List<ReleaseDatesResultDto>
){
    companion object{
        fun empty() = ReleaseDatesResponseDto(0, emptyList())
    }
}