package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.MovieReviewsResponseDto

interface ReviewsDataSource {
    suspend fun getMovieReviews(movieId: Int, page: Int, language: String): MovieReviewsResponseDto
}