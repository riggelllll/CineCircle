package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.VideosRepository
import javax.inject.Inject

class GetTvSeriesVideosUseCase @Inject constructor(private val repository: VideosRepository) {
    suspend operator fun invoke(tvSeriesId: Int, languageCode: String) =
        repository.getTvSeriesVideos(tvSeriesId, languageCode)
}