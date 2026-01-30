package com.koniukhov.cinecirclex.core.domain.model

sealed class MediaItem {
    abstract val id: Int
    abstract val title: String
    abstract val posterPath: String
    abstract val voteAverage: Float
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaItem

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}