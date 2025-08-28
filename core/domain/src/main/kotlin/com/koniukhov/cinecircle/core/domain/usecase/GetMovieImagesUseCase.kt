package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.ImagesRepository
import javax.inject.Inject

class GetMovieImagesUseCase @Inject constructor(private val repository: ImagesRepository) {
    suspend operator fun invoke(movieId: Int, language: String) = repository.getMovieImages(movieId, language)
}