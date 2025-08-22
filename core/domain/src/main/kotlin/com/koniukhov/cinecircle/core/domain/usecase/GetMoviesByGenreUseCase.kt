package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend operator fun invoke(genreId: Int, page: Int, language: String): List<Movie> {
        return repository.getMoviesByGenre(genreId, page, language)
    }
}