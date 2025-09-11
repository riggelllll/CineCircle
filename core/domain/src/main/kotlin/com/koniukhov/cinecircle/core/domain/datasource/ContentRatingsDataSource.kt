package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.ContentRatingsResponseDto

interface ContentRatingsDataSource {
    suspend fun getTvSeriesContentRatings(tvSeriesId: Int): ContentRatingsResponseDto
}