package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName
import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID

data class TvSeasonDetailsDto(
    @SerializedName("_id")
    val _id: String?,
    @SerializedName("air_date")
    val airDate: String?,
    val episodes: List<TvEpisodeDetailsDto>?,
    val name: String?,
    val overview: String?,
    val id: Int?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("vote_average")
    val voteAverage: Float?
){
    companion object{
        fun empty() = TvSeasonDetailsDto(
            _id = "",
            airDate = "",
            episodes = emptyList(),
            name = "",
            overview = "",
            id = INVALID_ID,
            posterPath = "",
            seasonNumber = 0,
            voteAverage = 0f
        )
    }
}