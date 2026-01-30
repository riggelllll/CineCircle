package com.koniukhov.cinecirclex.core.network.model

import com.google.gson.annotations.SerializedName
import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID

data class TvEpisodeDetailsDto(
    @SerializedName("air_date")
    val airDate: String?,
    val crew: List<CrewMemberDto>?,
    @SerializedName("episode_number")
    val episodeNumber: Int?,
    @SerializedName("guest_stars")
    val guestStars: List<CastMemberDto>?,
    val name: String?,
    val overview: String?,
    val id: Int?,
    @SerializedName("production_code")
    val productionCode: String?,
    val runtime: Int?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("still_path")
    val stillPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Float?,
    @SerializedName("vote_count")
    val voteCount: Int?
){
    companion object{
        fun empty() = TvEpisodeDetailsDto(
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
            voteAverage = 0.0f,
            voteCount = 0
        )
    }
}