package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.CreditsRepository
import javax.inject.Inject

class GetTvSeriesCreditsUseCase @Inject constructor(private val repository: CreditsRepository) {
    suspend operator fun invoke(tvSeriesId: Int, languageCode: String) =
        repository.getTvSeriesCredits(tvSeriesId, languageCode)
}