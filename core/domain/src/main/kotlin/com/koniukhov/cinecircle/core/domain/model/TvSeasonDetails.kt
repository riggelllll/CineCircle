package com.koniukhov.cinecircle.core.domain.model

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
)