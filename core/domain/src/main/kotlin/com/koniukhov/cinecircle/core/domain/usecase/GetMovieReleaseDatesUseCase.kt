package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.ReleaseDatesRepository
import javax.inject.Inject

class GetMovieReleaseDatesUseCase @Inject constructor(private val repository: ReleaseDatesRepository) {
    suspend operator fun invoke(movieId: Int) = repository.getMovieReleaseDates(movieId)
}