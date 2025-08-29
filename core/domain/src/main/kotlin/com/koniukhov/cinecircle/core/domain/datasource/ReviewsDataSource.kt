package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.MovieReviewResponseDto

interface ReviewsDataSource {
    suspend fun getMovieReviews(movieId: Int, page: Int, language: String): MovieReviewResponseDto
}