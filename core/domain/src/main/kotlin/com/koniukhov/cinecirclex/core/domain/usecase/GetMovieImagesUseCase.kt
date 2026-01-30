package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.ImagesRepository
import javax.inject.Inject

class GetMovieImagesUseCase @Inject constructor(private val repository: ImagesRepository) {
    suspend operator fun invoke(movieId: Int, language: String) = repository.getMovieImages(movieId, language)
}