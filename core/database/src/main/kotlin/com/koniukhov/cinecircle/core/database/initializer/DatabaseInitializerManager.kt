package com.koniukhov.cinecircle.core.database.initializer

import android.content.Context
import com.koniukhov.cinecircle.core.database.CineCircleDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: CineCircleDatabase
) {
    private val initializer by lazy {
        DatabaseInitializer(context, database)
    }

    suspend fun initialize() {
        initializer.initializeDatabase()
    }
}