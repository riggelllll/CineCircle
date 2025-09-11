package com.koniukhov.cinecircle.core.domain.model

data class MediaCredits(
    val cast: List<CastMember>,
    val crew: List<CrewMember>
)