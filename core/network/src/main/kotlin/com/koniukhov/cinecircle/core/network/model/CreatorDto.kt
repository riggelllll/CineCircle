package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName
import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID

data class CreatorDto(
    val id: Int = 0,
    @SerializedName("credit_id")
    val creditId: String,
    val name: String,
    val gender: Int = 0,
    @SerializedName("profile_path")
    val profilePath: String
){
    companion object{
        fun empty() = CreatorDto(INVALID_ID, "", "", 0, "")
    }
}