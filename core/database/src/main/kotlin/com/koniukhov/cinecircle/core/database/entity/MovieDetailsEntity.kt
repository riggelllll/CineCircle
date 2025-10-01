package com.koniukhov.cinecircle.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_details")
data class MovieDetailsEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val adult: Boolean,
    val backdropPath: String,
    val budget: Int,
    val homePage: String,
    val id: Int,
    val imdbId: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Float,
    val posterPath: String,
    val releaseDate: String,
    val revenue: Long,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val title: String,
    val voteAverage: Float,
    val voteCount: Int
)