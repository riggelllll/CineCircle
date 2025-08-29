package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.domain.datasource.ReviewsDataSource
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.MovieReviewResponseDto
import javax.inject.Inject

class RemoteMovieReviewsDataSource @Inject constructor(private val api: TMDBApi) : ReviewsDataSource {
    override suspend fun getMovieReviews(
        movieId: Int,
        page: Int,
        language: String
    ): MovieReviewResponseDto {
        return api.getMovieReviews(movieId, BuildConfig.API_KEY, page, language).body() ?: MovieReviewResponseDto.empty()
    }
}