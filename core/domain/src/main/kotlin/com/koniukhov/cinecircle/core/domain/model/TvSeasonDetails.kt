package com.koniukhov.cinecircle.core.domain.model

import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID

data class TvSeasonDetails(
    val _id: String,
    val airDate: String,
    val episodes: List<TvEpisodeDetails>,
    val name: String,
    val overview: String,
    val id: Int,
    val posterPath: String,
    val seasonNumber: Int,
    val voteAverage: Float
) {
    companion object {
        fun empty() = TvSeasonDetails(
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