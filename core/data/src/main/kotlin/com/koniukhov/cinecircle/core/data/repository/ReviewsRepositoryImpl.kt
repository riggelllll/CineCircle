package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteReviewsDataSourceImpl
import com.koniukhov.cinecircle.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.domain.model.MediaReview
import com.koniukhov.cinecircle.core.domain.repository.ReviewsRepository
import javax.inject.Inject

class ReviewsRepositoryImpl @Inject constructor(
    private val remoteReviewsDataSourceImpl: RemoteRemoteReviewsDataSourceImpl,
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
                tdo.results.map { it.toDomain() } },
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
                tdo.results.map { it.toDomain() } },
            localCall = { emptyList() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }
}