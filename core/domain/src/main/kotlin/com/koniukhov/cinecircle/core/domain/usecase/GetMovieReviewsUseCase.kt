package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.ReviewsRepository
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(private val reviewsRepository: ReviewsRepository) {
    suspend operator fun invoke(movieId: Int, page: Int, language: String) =
        reviewsRepository.getMovieReviews(movieId, page, language)
}