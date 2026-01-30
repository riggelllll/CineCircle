package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.network.model.CollectionDetailsDto

interface RemoteCollectionsDataSource {
    suspend fun getCollectionDetails(collectionId: Int, language: String): CollectionDetailsDto
}