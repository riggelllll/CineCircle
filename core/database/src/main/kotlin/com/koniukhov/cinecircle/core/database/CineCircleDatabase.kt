package com.koniukhov.cinecircle.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var INSTANCE: CineCircleDatabase? = null

        fun getDatabase(context: Context): CineCircleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CineCircleDatabase::class.java,
                    "cinecircle_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
