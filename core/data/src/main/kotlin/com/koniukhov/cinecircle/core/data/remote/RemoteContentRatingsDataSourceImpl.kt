package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.domain.datasource.ContentRatingsDataSource
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.ContentRatingsResponseDto
import javax.inject.Inject

class RemoteContentRatingsDataSourceImpl @Inject constructor(private val api: TMDBApi) : ContentRatingsDataSource {
    override suspend fun getTvSeriesContentRatings(
        tvSeriesId: Int
    ): ContentRatingsResponseDto {
        return api.getTvSeriesContentRatings(tvSeriesId, BuildConfig.API_KEY).body() ?: ContentRatingsResponseDto.empty()
    }
}