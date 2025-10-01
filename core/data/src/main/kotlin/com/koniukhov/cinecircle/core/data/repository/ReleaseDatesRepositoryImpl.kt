package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteReleaseDatesDataSourceImpl
import com.koniukhov.cinecircle.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.domain.model.ReleaseDateResult
import com.koniukhov.cinecircle.core.domain.repository.ReleaseDatesRepository
import javax.inject.Inject

class ReleaseDatesRepositoryImpl @Inject constructor(
    private val remoteReleaseDatesDataSourceImpl: RemoteRemoteReleaseDatesDataSourceImpl,
    private val networkStatusProvider: NetworkStatusProvider
) : ReleaseDatesRepository {
    override suspend fun getMovieReleaseDates(movieId: Int): List<ReleaseDateResult> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteReleaseDatesDataSourceImpl.getMovieReleaseDates(movieId)
                dto.results.map { it.toDomain() } },
            localCall = { emptyList() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }
}