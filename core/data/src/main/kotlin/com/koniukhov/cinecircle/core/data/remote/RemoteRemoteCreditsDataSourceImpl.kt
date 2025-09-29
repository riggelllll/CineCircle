package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.MediaCreditsDto
import javax.inject.Inject

class RemoteRemoteCreditsDataSourceImpl @Inject constructor(private val api: TMDBApi) : RemoteCreditsDatasource {
    override suspend fun getMovieCredits(
        movieId: Int,
        language: String
    ): MediaCreditsDto {
        return api.getMovieCredits(movieId, BuildConfig.API_KEY, language).body() ?: MediaCreditsDto.empty()
    }

    override suspend fun getTvSeriesCredits(
        tvSeriesId: Int,
        language: String
    ): MediaCreditsDto {
        return api.getTvSeriesCredits(tvSeriesId, BuildConfig.API_KEY, language).body() ?: MediaCreditsDto.empty()
    }
}