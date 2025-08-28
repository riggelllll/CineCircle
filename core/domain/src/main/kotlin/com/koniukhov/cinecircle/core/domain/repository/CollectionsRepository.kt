package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.CollectionDetails

interface CollectionsRepository {
    suspend fun getCollectionDetails(collectionId: Int, language: String): CollectionDetails
}