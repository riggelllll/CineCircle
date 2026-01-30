package com.koniukhov.cinecirclex.core.data.repository

import com.koniukhov.cinecirclex.core.data.mapper.toDomain
import com.koniukhov.cinecirclex.core.data.remote.RemoteContentRatingsDataSourceImpl
import com.koniukhov.cinecirclex.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.core.domain.model.ContentRating
import com.koniukhov.cinecirclex.core.domain.repository.ContentRatingsRepository
import javax.inject.Inject

class ContentRatingsRepositoryImpl @Inject constructor(
    private val remoteContentRatingsDataSourceImpl: RemoteContentRatingsDataSourceImpl,
    private val networkStatusProvider: NetworkStatusProvider
) : ContentRatingsRepository {
    override suspend fun getTvSeriesContentRatings(tvSeriesId: Int): List<ContentRating> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteContentRatingsDataSourceImpl.getTvSeriesContentRatings(tvSeriesId)
                dto.results?.map { it.toDomain() } },
            localCall = { emptyList() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }
}