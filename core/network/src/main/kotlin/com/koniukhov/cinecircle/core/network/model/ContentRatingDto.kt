package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName

data class ContentRatingDto (
    val descriptors: List<String>?,
    @SerializedName("iso_3166_1")
    val countryCode: String?,
    val rating: String?
)