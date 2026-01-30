package com.koniukhov.cinecirclex.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.koniukhov.cinecirclex.core.database.converter.MediaTypeConverter
import com.koniukhov.cinecirclex.core.database.dao.MediaListDao
import com.koniukhov.cinecirclex.core.database.dao.RatedMediaDao
import com.koniukhov.cinecirclex.core.database.entity.GenreEntity
import com.koniukhov.cinecirclex.core.database.entity.MediaListEntity
import com.koniukhov.cinecirclex.core.database.entity.MediaListItemEntity
import com.koniukhov.cinecirclex.core.database.entity.MovieDetailsEntity
import com.koniukhov.cinecirclex.core.database.entity.RatedMediaEntity
import com.koniukhov.cinecirclex.core.database.entity.TvSeriesDetailsEntity

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