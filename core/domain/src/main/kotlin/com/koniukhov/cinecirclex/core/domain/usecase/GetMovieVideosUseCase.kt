package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.VideosRepository
import javax.inject.Inject

class GetMovieVideosUseCase @Inject constructor(private val repository: VideosRepository) {
    suspend operator fun invoke(movieId: Int, language: String) = repository.getMovieVideos(movieId, language)
}