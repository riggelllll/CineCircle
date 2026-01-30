package com.koniukhov.cinecirclex.core.network.model

import com.google.gson.annotations.SerializedName
import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID

data class NetworkDto(
    val id: Int?,
    val name: String?,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("origin_country")
    val originCountry: String?
){
    companion object{
        fun empty() = NetworkDto(id = INVALID_ID, name = "", logoPath = "", originCountry = "")
    }
}