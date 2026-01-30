package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.MoviesRepository
import javax.inject.Inject

class GetSearchedMoviesUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend operator fun invoke(query: String, page: Int, language: String) =
        repository.getSearchedMovies(query, page, language)
}