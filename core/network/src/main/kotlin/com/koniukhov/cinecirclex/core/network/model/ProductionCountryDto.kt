package com.koniukhov.cinecirclex.core.network.model

import com.google.gson.annotations.SerializedName

data class ProductionCountryDto(
    @SerializedName("iso_3166_1")
    val countryCode: String?,
    val name: String?
)
