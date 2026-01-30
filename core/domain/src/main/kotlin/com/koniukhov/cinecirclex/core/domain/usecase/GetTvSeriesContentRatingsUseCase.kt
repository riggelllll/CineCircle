package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.ContentRatingsRepository
import javax.inject.Inject

class GetTvSeriesContentRatingsUseCase @Inject constructor(private val repository: ContentRatingsRepository) {
    suspend operator fun invoke(tvSeriesId: Int) = repository.getTvSeriesContentRatings(tvSeriesId)
}