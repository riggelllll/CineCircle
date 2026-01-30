package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.data.BuildConfig
import com.koniukhov.cinecirclex.core.network.api.TMDBApi
import com.koniukhov.cinecirclex.core.network.model.CollectionDetailsDto
import javax.inject.Inject

class RemoteCollectionsDataSourceImpl @Inject constructor(private val api: TMDBApi) : RemoteCollectionsDataSource {
    override suspend fun getCollectionDetails(
        collectionId: Int,
        language: String
    ): CollectionDetailsDto {
        return api.getCollectionDetails(collectionId, BuildConfig.API_KEY, language).body() ?: CollectionDetailsDto.empty()
    }

}