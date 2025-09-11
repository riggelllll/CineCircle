package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteContentRatingsDataSource
import com.koniukhov.cinecircle.core.domain.model.ContentRating
import com.koniukhov.cinecircle.core.domain.repository.ContentRatingsRepository
import javax.inject.Inject

class ContentRatingsRepositoryImpl @Inject constructor(private val remoteContentRatingsDataSource: RemoteContentRatingsDataSource) : ContentRatingsRepository {
    override suspend fun getTvSeriesContentRatings(tvSeriesId: Int): List<ContentRating> {
        val dto = remoteContentRatingsDataSource.getTvSeriesContentRatings(tvSeriesId)
        return dto.results.map { it.toDomain() }
    }
}