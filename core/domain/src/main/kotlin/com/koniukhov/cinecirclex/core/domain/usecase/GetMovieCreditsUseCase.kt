package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.CreditsRepository
import javax.inject.Inject

class GetMovieCreditsUseCase @Inject constructor(private val repository: CreditsRepository) {
    suspend operator fun invoke(movieId: Int, language: String) =
        repository.getMovieCredits(movieId, language)
}