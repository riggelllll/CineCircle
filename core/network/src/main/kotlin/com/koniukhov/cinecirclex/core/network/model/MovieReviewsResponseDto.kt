package com.koniukhov.cinecirclex.core.network.model

import com.google.gson.annotations.SerializedName
import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID

data class MovieReviewsResponseDto (
    @SerializedName("id")
    val id: Int?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<MediaReviewDto>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
){
    companion object{
        fun empty() = MovieReviewsResponseDto(
            id = INVALID_ID,
            page = 0,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )
    }
}