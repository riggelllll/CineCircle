package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName

data class ProductionCountryDto(
    @SerializedName("iso_3166_1")
    val isoName: String,
    val name: String
)
