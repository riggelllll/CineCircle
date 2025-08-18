package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import javax.inject.Inject

class GetUpcomingMoviesUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend operator fun invoke(page: Int, language: String) = repository.getUpcomingMovies(page, language)
}