package com.koniukhov.cinecircle.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.koniukhov.cinecircle.core.database.converter.MediaTypeConverter
import com.koniukhov.cinecircle.core.database.dao.MediaListDao
import com.koniukhov.cinecircle.core.database.dao.RatedMediaDao
import com.koniukhov.cinecircle.core.database.entity.GenreEntity
import com.koniukhov.cinecircle.core.database.entity.MediaListEntity
import com.koniukhov.cinecircle.core.database.entity.MediaListItemEntity
import com.koniukhov.cinecircle.core.database.entity.MovieDetailsEntity
import com.koniukhov.cinecircle.core.database.entity.RatedMediaEntity
import com.koniukhov.cinecircle.core.database.entity.TvSeriesDetailsEntity

@Database(
    entities = [
        MediaListEntity::class,
        MediaListItemEntity::class,
        MovieDetailsEntity::class,
        GenreEntity::class,
        TvSeriesDetailsEntity::class,
        RatedMediaEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MediaTypeConverter::class)
abstract class CineCircleDatabase : RoomDatabase() {

    abstract fun mediaListDao(): MediaListDao
    abstract fun ratedMediaDao(): RatedMediaDao
}