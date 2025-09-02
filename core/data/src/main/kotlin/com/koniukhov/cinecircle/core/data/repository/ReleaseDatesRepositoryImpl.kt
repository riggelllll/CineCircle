package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteReleaseDatesDataSource
import com.koniukhov.cinecircle.core.domain.model.ReleaseDateResult
import com.koniukhov.cinecircle.core.domain.repository.ReleaseDatesRepository
import javax.inject.Inject

class ReleaseDatesRepositoryImpl @Inject constructor(private val remoteReleaseDatesDataSource: RemoteReleaseDatesDataSource) : ReleaseDatesRepository {
    override suspend fun getMovieReleaseDates(movieId: Int): List<ReleaseDateResult> {
        val dto = remoteReleaseDatesDataSource.getMovieReleaseDates(movieId)
        return dto.results.map { it.toDomain() }
    }
}