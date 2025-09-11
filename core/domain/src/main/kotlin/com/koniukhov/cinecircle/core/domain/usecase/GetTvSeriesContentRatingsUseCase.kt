package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.ContentRatingsRepository
import javax.inject.Inject

class GetTvSeriesContentRatingsUseCase @Inject constructor(private val repository: ContentRatingsRepository) {
    suspend operator fun invoke(tvSeriesId: Int) = repository.getTvSeriesContentRatings(tvSeriesId)
}