package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteCreditsDataSource
import com.koniukhov.cinecircle.core.domain.model.MediaCredits
import com.koniukhov.cinecircle.core.domain.repository.CreditsRepository
import javax.inject.Inject

class CreditsRepositoryImpl @Inject constructor(
    private val remoteCreditsDataSource: RemoteCreditsDataSource)
    : CreditsRepository{
    override suspend fun getMovieCredits(
        movieId: Int,
        language: String
    ): MediaCredits {
        val dto = remoteCreditsDataSource.getMovieCredits(movieId, language)
        return dto.toDomain()
    }

    override suspend fun getTvSeriesCredits(
        tvSeriesId: Int,
        language: String
    ): MediaCredits {
        val dto = remoteCreditsDataSource.getTvSeriesCredits(tvSeriesId, language)
        return dto.toDomain()
    }
}