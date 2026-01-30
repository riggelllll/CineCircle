package com.koniukhov.cinecirclex.core.network.model

import com.google.gson.annotations.SerializedName

data class ReleaseDatesDto(
    val certification: String?,
    val descriptors: List<String>?,
    @SerializedName("iso_639_1")
    val languageCode: String?,
    val note: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val type: Int?
)