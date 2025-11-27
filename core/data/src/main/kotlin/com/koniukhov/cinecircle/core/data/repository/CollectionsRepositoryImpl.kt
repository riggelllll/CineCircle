package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteCollectionsDataSourceImpl
import com.koniukhov.cinecircle.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.domain.model.CollectionDetails
import com.koniukhov.cinecircle.core.domain.repository.CollectionsRepository
import javax.inject.Inject

class CollectionsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteCollectionsDataSourceImpl,
    private val networkStatusProvider: NetworkStatusProvider
) : CollectionsRepository {
    override suspend fun getCollectionDetails(
        collectionId: Int,
        language: String
    ): CollectionDetails {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val tdo = remoteDataSource.getCollectionDetails(collectionId, language)
                tdo.toDomain() },
            localCall = { CollectionDetails.empty() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: CollectionDetails.empty()
    }
}