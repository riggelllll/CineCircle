package com.koniukhov.cinecirclex.core.network.model

import com.google.gson.annotations.SerializedName

data class CrewMemberDto (
    val adult: Boolean?,
    val gender: Int?,
    val id: Int?,
    @SerializedName("known_for_department")
    val knownForDepartment: String?,
    val name: String?,
    @SerializedName("original_name")
    val originalName: String?,
    val popularity: Float?,
    @SerializedName("profile_path")
    val profilePath: String?,
    @SerializedName("credit_id")
    val creditId: String?,
    val department: String?,
    val job: String?
)