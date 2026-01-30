package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.data.BuildConfig
import com.koniukhov.cinecirclex.core.network.api.TMDBApi
import com.koniukhov.cinecirclex.core.network.model.ContentRatingsResponseDto
import javax.inject.Inject

class RemoteContentRatingsDataSourceImpl @Inject constructor(private val api: TMDBApi) : RemoteContentRatingsDataSource {
    override suspend fun getTvSeriesContentRatings(
        tvSeriesId: Int
    ): ContentRatingsResponseDto {
        return api.getTvSeriesContentRatings(tvSeriesId, BuildConfig.API_KEY).body() ?: ContentRatingsResponseDto.empty()
    }
}