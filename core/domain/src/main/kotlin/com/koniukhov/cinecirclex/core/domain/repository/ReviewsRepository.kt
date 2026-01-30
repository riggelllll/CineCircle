package com.koniukhov.cinecirclex.core.domain.repository

import com.koniukhov.cinecirclex.core.domain.model.MediaReview

interface ReviewsRepository {
    suspend fun getMovieReviews(movieId: Int, page: Int, language: String): List<MediaReview>
    suspend fun getTvSeriesReviews(tvSeriesId: Int, page: Int, language: String): List<MediaReview>
}