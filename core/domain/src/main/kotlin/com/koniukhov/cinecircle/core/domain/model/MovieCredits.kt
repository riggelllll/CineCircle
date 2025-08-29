package com.koniukhov.cinecircle.core.domain.model

data class MovieCredits(
    val cast: List<CastMember>,
    val crew: List<CrewMember>
)