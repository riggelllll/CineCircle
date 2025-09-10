package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.ImagesRepository
import javax.inject.Inject

class GetTvSeriesImagesUseCase @Inject constructor(private val repository: ImagesRepository) {
    suspend operator fun invoke(tvSeriesId: Int, language: String) = repository.getTvSeriesImages(tvSeriesId, language)
}