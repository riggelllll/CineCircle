package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.ReviewsRepository
import javax.inject.Inject

class GetTvSeriesReviewsUseCase @Inject constructor(private val repository: ReviewsRepository) {
    suspend operator fun invoke(tvSeriesId: Int, language: String, page: Int) =
        repository.getTvSeriesReviews(tvSeriesId, page, language)
}