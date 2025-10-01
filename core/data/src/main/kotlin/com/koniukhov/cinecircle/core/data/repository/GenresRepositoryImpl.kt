package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteGenresDataSourceImpl
import com.koniukhov.cinecircle.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.repository.GenresRepository
import javax.inject.Inject

class GenresRepositoryImpl @Inject constructor(
    private val remoteGenresDataSourceImpl: RemoteRemoteGenresDataSourceImpl,
    private val networkStatusProvider: NetworkStatusProvider
) : GenresRepository{
    override suspend fun getMoviesGenreList(language: String): List<Genre> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteGenresDataSourceImpl.getMoviesGenreList(language)
                dto.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }

    override suspend fun getTvSeriesGenreList(language: String): List<Genre> {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteGenresDataSourceImpl.getTvSeriesGenreList(language)
                dto.map { it.toDomain() } },
            localCall = { null },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: emptyList()
    }
}