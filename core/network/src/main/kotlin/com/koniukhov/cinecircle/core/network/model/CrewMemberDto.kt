package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName

data class CrewMemberDto (
    val adult: Boolean = true,
    val gender: Int = 0,
    val id: Int = 0,
    @SerializedName("known_for_department")
    val knownForDepartment: String,
    val name: String,
    @SerializedName("original_name")
    val originalName: String,
    val popularity: Double = 0.0,
    @SerializedName("profile_path")
    val profilePath: String,
    @SerializedName("credit_id")
    val creditId: String,
    val department: String,
    val job: String
)