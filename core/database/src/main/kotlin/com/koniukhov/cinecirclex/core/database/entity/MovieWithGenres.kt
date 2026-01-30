package com.koniukhov.cinecirclex.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class MovieWithGenres(
    @Embedded val movie: MovieDetailsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "mediaId"
    )
    val genres: List<GenreEntity>
)