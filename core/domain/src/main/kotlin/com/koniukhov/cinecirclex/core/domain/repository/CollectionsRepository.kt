package com.koniukhov.cinecirclex.core.domain.repository

import com.koniukhov.cinecirclex.core.domain.model.CollectionDetails

interface CollectionsRepository {
    suspend fun getCollectionDetails(collectionId: Int, language: String): CollectionDetails
}