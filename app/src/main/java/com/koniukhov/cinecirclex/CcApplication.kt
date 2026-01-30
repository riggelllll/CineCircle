package com.koniukhov.cinecirclex

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.koniukhov.cinecirclex.core.common.util.ImageCacheManager
import com.koniukhov.cinecirclex.core.database.initializer.DatabaseInitializerManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class CcApplication : Application() {

    @Inject
    lateinit var databaseInitializer: DatabaseInitializerManager

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        initCrashlytics()
        initLogging()
        initializeDatabase()
        performImageCacheCleanup()
    }

    private fun initCrashlytics() {
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
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

    private fun performImageCacheCleanup() {
        applicationScope.launch {
            try {
                ImageCacheManager.performAutoCleanupIfNeeded(this@CcApplication)
                Timber.d("Image cache cleanup check completed")
            } catch (e: Exception) {
                Timber.e(e, "Failed to perform image cache cleanup")
            }
        }
    }
}