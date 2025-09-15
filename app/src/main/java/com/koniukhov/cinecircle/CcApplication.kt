package com.koniukhov.cinecircle

import android.app.Application
import com.koniukhov.cinecircle.core.database.initializer.DatabaseInitializerManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class CcApplication : Application(){

    @Inject
    lateinit var databaseInitializer: DatabaseInitializerManager

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        initLogging()
        initializeDatabase()
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initializeDatabase() {
        applicationScope.launch {
            try {
                databaseInitializer.initialize()
                Timber.d("Database initialized successfully")
            } catch (e: Exception) {
                Timber.e(e, "Failed to initialize database")
            }
        }
    }
}