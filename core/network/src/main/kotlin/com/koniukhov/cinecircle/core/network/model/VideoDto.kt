package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName

data class VideoDto(
    @SerializedName("iso_639_1")
    val languageCode: String,
    @SerializedName("iso_3166_1")
    val countryCode: String,
    val name: String,
    val key: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    @SerializedName("published_at")
    val publishedAt: String,
    val id: String
)