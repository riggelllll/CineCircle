package com.koniukhov.cinecircle.core.database.model

data class MediaListWithCount(
    val id: Long,
    val name: String,
    val isDefault: Boolean,
    val createdAt: Long,
    val itemCount: Int
)

data class MediaListItem(
    val id: Long,
    val listId: Long,
    val mediaId: Int,
    val mediaType: Int,
    val addedAt: Long
)