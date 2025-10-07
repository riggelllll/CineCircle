package com.koniukhov.cinecircle.core.network.model

import com.google.gson.annotations.SerializedName

data class MoviesResponseDto(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<MovieDto>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
){
    companion object {
        fun empty() = MoviesResponseDto(
            page = 0,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )
    }
}