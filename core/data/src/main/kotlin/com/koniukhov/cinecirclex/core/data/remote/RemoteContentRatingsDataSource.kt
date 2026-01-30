package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.network.model.ContentRatingsResponseDto

interface RemoteContentRatingsDataSource {
    suspend fun getTvSeriesContentRatings(tvSeriesId: Int): ContentRatingsResponseDto
}