package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName

data class CollectionMediaDto(
    val adult: Boolean = true,

    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    val id: Int = 0,

    val title: String? = null,

    @SerializedName("original_language")
    val originalLanguage: String? = null,

    @SerializedName("original_title")
    val originalTitle: String? = null,

    val overview: String? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("media_type")
    val mediaType: String? = null,

    @SerializedName("genre_ids")
    val genreIds: List<Int>? = null,

    val popularity: Double = 0.0,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    val video: Boolean = true,

    @SerializedName("vote_average")
    val voteAverage: Double = 0.0,

    @SerializedName("vote_count")
    val voteCount: Int = 0
)