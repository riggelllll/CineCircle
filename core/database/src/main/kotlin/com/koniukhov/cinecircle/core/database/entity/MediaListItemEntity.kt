package com.koniukhov.cinecircle.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "media_list_items",
    foreignKeys = [
        ForeignKey(
            entity = MediaListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("listId"),
        Index("mediaId"),
        Index("mediaType"),
        Index(value = ["listId", "mediaId", "mediaType"], unique = true)
    ]
)
data class MediaListItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val listId: Long,
    val mediaId: Int,
    val mediaType: Int,
)