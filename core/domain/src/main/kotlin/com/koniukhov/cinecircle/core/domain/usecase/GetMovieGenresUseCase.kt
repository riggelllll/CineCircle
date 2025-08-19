package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.GenresRepository
import javax.inject.Inject

class GetMovieGenresUseCase @Inject constructor(private val repository: GenresRepository) {
    suspend operator fun invoke(language: String) = repository.getMoviesGenreList(language)
}