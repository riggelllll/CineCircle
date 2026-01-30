package com.koniukhov.cinecirclex.core.data.repository

import com.koniukhov.cinecirclex.core.data.mapper.toDomain
import com.koniukhov.cinecirclex.core.data.remote.RemoteReviewsDataSourceImpl
import com.koniukhov.cinecirclex.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.core.domain.model.MediaReview
import com.koniukhov.cinecirclex.core.domain.repository.ReviewsRepository
import javax.inject.Inject

class ReviewsRepositoryImpl @Inject constructor(
    private val remoteReviewsDataSourceImpl: RemoteReviewsDataSourceImpl,
    private val networkStatusProvider: NetworkStatusProvider
) : ReviewsRepository {
    override suspend fun getMovieReviews(
        movieId: Int,
        page: Int,
        language: String
    ): List<MediaReview> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val tdo = remoteReviewsDataSourceImpl.getMovieReviews(movieId, page, language)
                tdo.results?.map { it.toDomain() } },
            localCall = { emptyList() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getTvSeriesReviews(
        tvSeriesId: Int,
        page: Int,
        language: String
    ): List<MediaReview> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val tdo = remoteReviewsDataSourceImpl.getTvSeriesReviews(tvSeriesId, page, language)
                tdo.results?.map { it.toDomain() } },
            localCall = { emptyList() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }
}