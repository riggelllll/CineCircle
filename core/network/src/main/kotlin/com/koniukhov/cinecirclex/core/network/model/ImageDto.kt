package com.koniukhov.cinecirclex.core.network.model

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("aspect_ratio")
    val aspectRatio: Float?,
    @SerializedName("file_path")
    val filePath: String?,
    val height: Int?,
    @SerializedName("iso_639_1")
    val countryCode: String?,
    @SerializedName("vote_average")
    val voteAverage: Float?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    val width: Int?
)
