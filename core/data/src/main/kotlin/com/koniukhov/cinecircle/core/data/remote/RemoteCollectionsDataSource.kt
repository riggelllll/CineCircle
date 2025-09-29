package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.network.model.CollectionDetailsDto

interface RemoteCollectionsDataSource {
    suspend fun getCollectionDetails(collectionId: Int, language: String): CollectionDetailsDto
}