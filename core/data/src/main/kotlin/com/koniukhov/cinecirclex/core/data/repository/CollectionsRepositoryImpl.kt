package com.koniukhov.cinecirclex.core.data.repository

import com.koniukhov.cinecirclex.core.data.mapper.toDomain
import com.koniukhov.cinecirclex.core.data.remote.RemoteCollectionsDataSourceImpl
import com.koniukhov.cinecirclex.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.core.domain.model.CollectionDetails
import com.koniukhov.cinecirclex.core.domain.repository.CollectionsRepository
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