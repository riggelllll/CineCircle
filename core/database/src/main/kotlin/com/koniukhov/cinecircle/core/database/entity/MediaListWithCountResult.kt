package com.koniukhov.cinecircle.core.database.entity

import androidx.room.Embedded

data class MediaListWithCountResult(
    @Embedded
    val mediaList: MediaListEntity,
    val itemCount: Int
)