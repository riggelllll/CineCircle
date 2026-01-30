package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.CollectionsRepository
import javax.inject.Inject

class GetCollectionDetailsUseCase @Inject constructor(private val repository: CollectionsRepository) {
    suspend operator fun invoke(collectionId: Int, language: String) = repository.getCollectionDetails(collectionId, language)
}