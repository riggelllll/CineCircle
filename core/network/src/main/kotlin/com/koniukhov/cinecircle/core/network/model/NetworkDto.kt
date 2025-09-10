package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName
import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID

data class NetworkDto(
    val id: Int = 0,
    val name: String,
    @SerializedName("logo_path")
    val logoPath: String,
    @SerializedName("origin_country")
    val originCountry: String
){
    companion object{
        fun empty() = NetworkDto(id = INVALID_ID, name = "", logoPath = "", originCountry = "")
    }
}