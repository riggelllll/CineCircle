package com.koniukhov.cinecircle.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tv_series_details")
data class TvSeriesDetailsEntity (
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val adult: Boolean,
    val backdropPath: String,
    val firstAirDate: String,
    val homepage: String,
    val id: Int,
    val inProduction: Boolean,
    val lastAirDate: String,
    val name: String,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Float,
    val posterPath: String,
    val status: String,
    val tagline: String,
    val type: String,
    val voteAverage: Float,
    val voteCount: Int
)