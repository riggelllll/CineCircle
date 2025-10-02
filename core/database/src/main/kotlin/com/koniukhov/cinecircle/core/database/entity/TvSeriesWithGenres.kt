package com.koniukhov.cinecircle.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TvSeriesWithGenres(
    @Embedded
    val tvSeries: TvSeriesDetailsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "mediaId"
    )
    val genres: List<GenreEntity>
)