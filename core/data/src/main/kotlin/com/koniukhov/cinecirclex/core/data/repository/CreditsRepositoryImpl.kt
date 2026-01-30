package com.koniukhov.cinecirclex.core.data.repository

import com.koniukhov.cinecirclex.core.data.mapper.toDomain
import com.koniukhov.cinecirclex.core.data.remote.RemoteCreditsDataSourceImpl
import com.koniukhov.cinecirclex.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.core.domain.model.MediaCredits
import com.koniukhov.cinecirclex.core.domain.repository.CreditsRepository
import javax.inject.Inject

class CreditsRepositoryImpl @Inject constructor(
    private val remoteCreditsDataSourceImpl: RemoteCreditsDataSourceImpl,
    private val networkStatusProvider: NetworkStatusProvider
) : CreditsRepository{
    override suspend fun getMovieCredits(
        movieId: Int,
        language: String
    ): MediaCredits {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteCreditsDataSourceImpl.getMovieCredits(movieId, language)
                dto.toDomain() },
            localCall = { MediaCredits.empty() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: MediaCredits.empty()
    }

    override suspend fun getTvSeriesCredits(
        tvSeriesId: Int,
        language: String
    ): MediaCredits {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteCreditsDataSourceImpl.getTvSeriesCredits(tvSeriesId, language)
                dto.toDomain() },
            localCall = { MediaCredits.empty() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: MediaCredits.empty()
    }
}