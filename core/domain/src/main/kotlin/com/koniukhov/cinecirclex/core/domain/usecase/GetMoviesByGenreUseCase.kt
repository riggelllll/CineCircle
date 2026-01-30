package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.Movie
import com.koniukhov.cinecirclex.core.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend operator fun invoke(genreId: Int, page: Int, language: String): List<Movie> {
        return repository.getMoviesByGenre(genreId, page, language)
    }
}