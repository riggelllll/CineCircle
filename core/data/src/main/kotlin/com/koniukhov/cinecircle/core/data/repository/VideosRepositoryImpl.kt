package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteMovieVideosDataSource
import com.koniukhov.cinecircle.core.domain.model.MovieVideos
import com.koniukhov.cinecircle.core.domain.repository.VideosRepository
import javax.inject.Inject

class VideosRepositoryImpl @Inject constructor(private val remoteImagesDataSource: RemoteMovieVideosDataSource) : VideosRepository{
    override suspend fun getMovieVideos(
        movieId: Int,
        language: String
    ): MovieVideos {
        val dto = remoteImagesDataSource.getMovieVideos(movieId, language)
        return dto.toDomain()
    }
}