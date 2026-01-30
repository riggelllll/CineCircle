package com.koniukhov.cinecirclex.core.data.repository

import com.koniukhov.cinecirclex.core.data.mapper.toDomain
import com.koniukhov.cinecirclex.core.data.remote.RemoteReleaseDatesDataSourceImpl
import com.koniukhov.cinecirclex.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.core.domain.model.ReleaseDateResult
import com.koniukhov.cinecirclex.core.domain.repository.ReleaseDatesRepository
import javax.inject.Inject

class ReleaseDatesRepositoryImpl @Inject constructor(
    private val remoteReleaseDatesDataSourceImpl: RemoteReleaseDatesDataSourceImpl,
    private val networkStatusProvider: NetworkStatusProvider
) : ReleaseDatesRepository {
    override suspend fun getMovieReleaseDates(movieId: Int): List<ReleaseDateResult> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteReleaseDatesDataSourceImpl.getMovieReleaseDates(movieId)
                dto.results?.map { it.toDomain() } },
            localCall = { emptyList() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }
}