package com.koniukhov.cinecircle.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MediaItemEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val mediaId: Long,
    var filePath: String,
    var imageType: ImageType
)