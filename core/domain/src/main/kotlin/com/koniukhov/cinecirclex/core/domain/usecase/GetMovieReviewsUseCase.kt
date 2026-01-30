package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.ReviewsRepository
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(private val reviewsRepository: ReviewsRepository) {
    suspend operator fun invoke(movieId: Int, page: Int, language: String) =
        reviewsRepository.getMovieReviews(movieId, page, language)
}