package com.koniukhov.cinecirclex.core.network.model

import com.google.gson.annotations.SerializedName

data class ReleaseDatesResultDto(
    @SerializedName("iso_3166_1")
    val countryCode: String?,
    @SerializedName("release_dates")
    val releaseDates: List<ReleaseDatesDto>?
)