package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.GenresRepository
import javax.inject.Inject

class GetTvSeriesGenresUseCase @Inject constructor(private val repository: GenresRepository) {
    suspend operator fun invoke(language: String) = repository.getTvSeriesGenreList(language)
}