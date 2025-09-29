package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteGenresDataSourceImpl
import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.repository.GenresRepository
import javax.inject.Inject

class GenresRepositoryImpl @Inject constructor(private val remoteGenresDataSourceImpl: RemoteGenresDataSourceImpl) : GenresRepository{
    override suspend fun getMoviesGenreList(language: String): List<Genre> {
        val dto = remoteGenresDataSourceImpl.getMoviesGenreList(language)
        return dto.map { it.toDomain() }
    }

    override suspend fun getTvSeriesGenreList(language: String): List<Genre> {
        val dto = remoteGenresDataSourceImpl.getTvSeriesGenreList(language)
        return dto.map { it.toDomain() }
    }
}