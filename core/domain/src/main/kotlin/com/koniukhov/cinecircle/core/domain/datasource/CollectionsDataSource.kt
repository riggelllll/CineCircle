package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.CollectionDetailsDto

interface CollectionsDataSource {
    suspend fun getCollectionDetails(collectionId: Int, language: String): CollectionDetailsDto
}