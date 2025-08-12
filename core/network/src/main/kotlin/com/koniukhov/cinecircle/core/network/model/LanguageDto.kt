package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName

data class LanguageDto(
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("iso_639_1")
    val isoName: String,
    val name: String
)