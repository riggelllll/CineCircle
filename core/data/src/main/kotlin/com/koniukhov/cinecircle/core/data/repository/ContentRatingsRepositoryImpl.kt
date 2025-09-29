package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteContentRatingsDataSourceImpl
import com.koniukhov.cinecircle.core.domain.model.ContentRating
import com.koniukhov.cinecircle.core.domain.repository.ContentRatingsRepository
import javax.inject.Inject

class ContentRatingsRepositoryImpl @Inject constructor(private val remoteContentRatingsDataSourceImpl: RemoteRemoteContentRatingsDataSourceImpl) : ContentRatingsRepository {
    override suspend fun getTvSeriesContentRatings(tvSeriesId: Int): List<ContentRating> {
        val dto = remoteContentRatingsDataSourceImpl.getTvSeriesContentRatings(tvSeriesId)
        return dto.results.map { it.toDomain() }
    }
}