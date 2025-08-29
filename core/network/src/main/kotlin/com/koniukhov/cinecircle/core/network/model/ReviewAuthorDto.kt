package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName

data class ReviewAuthorDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatar_path")
    val avatarPath: String,
    @SerializedName("rating")
    val rating: String
)