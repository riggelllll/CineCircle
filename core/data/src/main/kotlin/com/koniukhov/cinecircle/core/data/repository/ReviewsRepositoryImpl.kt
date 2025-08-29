package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteMovieReviewsDataSource
import com.koniukhov.cinecircle.core.domain.model.MovieReview
import com.koniukhov.cinecircle.core.domain.repository.ReviewsRepository
import javax.inject.Inject

class ReviewsRepositoryImpl @Inject constructor(private val remoteMovieReviewsDataSource: RemoteMovieReviewsDataSource) : ReviewsRepository {
    override suspend fun getMovieReviews(
        movieId: Int,
        page: Int,
        language: String
    ): List<MovieReview> {
        val tdo = remoteMovieReviewsDataSource.getMovieReviews(movieId, page, language)
        return tdo.results.map { it.toDomain() }
    }
}