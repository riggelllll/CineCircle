package com.koniukhov.cinecircle.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.koniukhov.cinecircle.core.database.dao.MediaListDao
import com.koniukhov.cinecircle.core.database.entity.MediaListEntity
import com.koniukhov.cinecircle.core.database.entity.MediaListItemEntity

@Database(
    entities = [
        MediaListEntity::class,
        MediaListItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CineCircleDatabase : RoomDatabase() {

    abstract fun mediaListDao(): MediaListDao
}
