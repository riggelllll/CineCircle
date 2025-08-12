package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName

data class CompanyDto(
    val id: UInt,
    @SerializedName("logo_path")
    val logoPath: String,
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String
)
