package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend operator fun invoke(movieId: Int, language: String) = repository.getMovieDetails(movieId, language)
}