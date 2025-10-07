package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName
import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID

data class CreatorDto(
    val id: Int?,
    @SerializedName("credit_id")
    val creditId: String?,
    val name: String?,
    val gender: Int?,
    @SerializedName("profile_path")
    val profilePath: String?
){
    companion object{
        fun empty() = CreatorDto(INVALID_ID, "", "", 0, "")
    }
}