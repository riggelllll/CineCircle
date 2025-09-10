package com.koniukhov.cinecircle.core.domain.model

import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID

data class TvEpisodeDetails(
    val airDate: String,
    val crew: List<CrewMember>,
    val episodeNumber: Int,
    val guestStars: List<CastMember>,
    val name: String,
    val overview: String,
    val id: Int,
    val productionCode: String,
    val runtime: Int,
    val seasonNumber: Int,
    val stillPath: String,
    val voteAverage: Float,
    val voteCount: Int
){
    companion object{
        fun empty() = TvEpisodeDetails(
            airDate = "",
            crew = emptyList(),
            episodeNumber = 0,
            guestStars = emptyList(),
            name = "",
            overview = "",
            id = INVALID_ID,
            productionCode = "",
            runtime = 0,
            seasonNumber = 0,
            stillPath = "",
            voteAverage = 0f,
            voteCount = 0
        )
    }
}