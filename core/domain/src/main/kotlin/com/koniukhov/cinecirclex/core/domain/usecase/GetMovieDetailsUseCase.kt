package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend operator fun invoke(movieId: Int, language: String) = repository.getMovieDetails(movieId, language)
}