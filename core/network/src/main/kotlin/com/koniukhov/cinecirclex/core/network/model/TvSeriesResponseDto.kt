package com.koniukhov.cinecirclex.core.network.model

import com.google.gson.annotations.SerializedName

data class TvSeriesResponseDto(
    @SerializedName("page") val page: Int?,
    @SerializedName("results") val results: List<TvSeriesDto>?,
    @SerializedName("total_pages") val totalPages: Int?,
    @SerializedName("total_results") val totalResults: Int?
){
    companion object {
        fun empty() = TvSeriesResponseDto(
            page = 0,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )
    }
}
