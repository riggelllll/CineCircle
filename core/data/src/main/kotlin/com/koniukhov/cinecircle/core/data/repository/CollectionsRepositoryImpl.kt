package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteCollectionsDataSourceImpl
import com.koniukhov.cinecircle.core.domain.model.CollectionDetails
import com.koniukhov.cinecircle.core.domain.repository.CollectionsRepository
import javax.inject.Inject

class CollectionsRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteCollectionsDataSourceImpl) : CollectionsRepository {
    override suspend fun getCollectionDetails(
        collectionId: Int,
        language: String
    ): CollectionDetails {
        val tdo = remoteDataSource.getCollectionDetails(collectionId, language)
        return tdo.toDomain()
    }
}