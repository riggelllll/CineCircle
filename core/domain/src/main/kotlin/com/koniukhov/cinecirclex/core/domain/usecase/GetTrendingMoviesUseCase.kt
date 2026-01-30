package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.MoviesRepository
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend operator fun invoke(page: Int, language: String) = repository.getTrendingMovies(page, language)
}