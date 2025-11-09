package com.koniukhov.cinecircle.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rated_medias")
data class RatedMediaEntity(
    @PrimaryKey
    val mediaId: Long,
    val rating: Float
)