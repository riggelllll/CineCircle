package com.koniukhov.cinecirclex.core.domain.model

data class MediaCredits(
    val cast: List<CastMember>,
    val crew: List<CrewMember>
) {
    companion object {
        fun empty() = MediaCredits(emptyList(), emptyList())
    }
}