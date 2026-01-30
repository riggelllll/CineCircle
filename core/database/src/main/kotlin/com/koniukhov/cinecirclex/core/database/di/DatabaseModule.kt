package com.koniukhov.cinecirclex.core.database.di

import android.content.Context
import androidx.room.Room
import com.koniukhov.cinecirclex.core.database.CineCircleDatabase
import com.koniukhov.cinecirclex.core.database.repository.MediaListRepository
import com.koniukhov.cinecirclex.core.database.dao.MediaListDao
import com.koniukhov.cinecirclex.core.database.dao.RatedMediaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CineCircleDatabase {
        return Room.databaseBuilder(
            context,
            CineCircleDatabase::class.java,
            "cinecircle_database"
        ).build()
    }

    @Provides
    fun provideMediaListDao(database: CineCircleDatabase): MediaListDao {
        return database.mediaListDao()
    }

    @Provides
    fun provideRatedMediaDao(database: CineCircleDatabase): RatedMediaDao {
        return database.ratedMediaDao()
    }

    @Provides
    @Singleton
    fun provideMediaListRepository(
        mediaListDao: MediaListDao
    ): MediaListRepository {
        return MediaListRepository(mediaListDao)
    }
}