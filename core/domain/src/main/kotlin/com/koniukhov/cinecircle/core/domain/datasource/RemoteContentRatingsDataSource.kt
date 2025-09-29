package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.ContentRatingsResponseDto

interface RemoteContentRatingsDataSource {
    suspend fun getTvSeriesContentRatings(tvSeriesId: Int): ContentRatingsResponseDto
}