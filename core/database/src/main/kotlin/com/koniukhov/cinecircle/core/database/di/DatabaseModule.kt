package com.koniukhov.cinecircle.core.database.di

import android.content.Context
import androidx.room.Room
import com.koniukhov.cinecircle.core.database.CineCircleDatabase
import com.koniukhov.cinecircle.core.database.dao.MediaListDao
import com.koniukhov.cinecircle.core.database.repository.MediaListRepository
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
    @Singleton
    fun provideMediaListRepository(
        mediaListDao: MediaListDao
    ): MediaListRepository {
        return MediaListRepository(mediaListDao)
    }
}