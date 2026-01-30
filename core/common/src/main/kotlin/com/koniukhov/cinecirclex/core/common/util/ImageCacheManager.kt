package com.koniukhov.cinecirclex.core.common.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.Locale

private val Context.imageCacheDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "image_cache_prefs"
)

object ImageCacheManager {

    private val KEY_LAST_CLEANUP = longPreferencesKey("last_cleanup_timestamp")
    private val KEY_LAST_CLEANUP_SIZE = longPreferencesKey("last_cleanup_size")

    private const val MAX_CACHE_SIZE_BYTES = 50L * 1024 * 1024
    private const val CLEANUP_INTERVAL_DAYS = 7
    private const val FILE_MAX_AGE_DAYS = 30

    suspend fun performAutoCleanupIfNeeded(context: Context) {
        withContext(Dispatchers.IO) {
            try {
                val lastCleanup = context.imageCacheDataStore.data
                    .map { preferences -> preferences[KEY_LAST_CLEANUP] ?: 0L }
                    .first()

                val currentTime = System.currentTimeMillis()
                val daysSinceCleanup = (currentTime - lastCleanup) / (1000 * 60 * 60 * 24)

                val currentSize = getCacheSize(context)
                val shouldCleanupByTime = daysSinceCleanup >= CLEANUP_INTERVAL_DAYS
                val shouldCleanupBySize = currentSize > MAX_CACHE_SIZE_BYTES

                when {
                    shouldCleanupBySize -> {
                        Timber.d("Auto-cleanup triggered: cache size (${formatSize(currentSize)}) exceeds limit (${formatSize(MAX_CACHE_SIZE_BYTES)})")
                        clearOldFiles(context)
                        if (getCacheSize(context) > MAX_CACHE_SIZE_BYTES) {
                            clearAll(context)
                        }
                    }
                    shouldCleanupByTime -> {
                        Timber.d("Auto-cleanup triggered: $daysSinceCleanup days since last cleanup")
                        clearOldFiles(context)
                    }
                    else -> {
                        Timber.d("Auto-cleanup not needed. Size: ${formatSize(currentSize)}, Days since cleanup: $daysSinceCleanup")
                        return@withContext
                    }
                }

                context.imageCacheDataStore.edit { preferences ->
                    preferences[KEY_LAST_CLEANUP] = currentTime
                    preferences[KEY_LAST_CLEANUP_SIZE] = getCacheSize(context)
                }

            } catch (e: Exception) {
                Timber.e(e, "Failed to perform auto cleanup")
            }
        }
    }

    private suspend fun clearOldFiles(context: Context) {
        withContext(Dispatchers.IO) {
            try {
                val cacheDirs = listOf(
                    File(context.cacheDir, "image_cache"),
                    File(context.cacheDir, "coil3_disk_cache"),
                    File(context.cacheDir, "image_loader_disk_cache")
                )

                val currentTime = System.currentTimeMillis()
                val maxAge = FILE_MAX_AGE_DAYS * 24 * 60 * 60 * 1000L
                var totalDeletedCount = 0
                var totalFreedSpace = 0L

                cacheDirs.forEach { cacheDir ->
                    if (!cacheDir.exists() || !cacheDir.isDirectory) return@forEach

                    cacheDir.walkTopDown()
                        .filter { it.isFile }
                        .forEach { file ->
                            val age = currentTime - file.lastModified()
                            if (age > maxAge) {
                                val size = file.length()
                                if (file.delete()) {
                                    totalDeletedCount++
                                    totalFreedSpace += size
                                }
                            }
                        }
                }

                if (totalDeletedCount > 0) {
                    Timber.d("Deleted $totalDeletedCount old cache files, freed ${formatSize(totalFreedSpace)}")
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to clear old cache files")
            }
        }
    }

    suspend fun clearAll(context: Context) {
        withContext(Dispatchers.IO) {
            try {
                val cacheDirs = listOf(
                    File(context.cacheDir, "image_cache"),
                    File(context.cacheDir, "coil3_disk_cache"),
                    File(context.cacheDir, "image_loader_disk_cache")
                )

                cacheDirs.forEach { cacheDir ->
                    if (cacheDir.exists() && cacheDir.isDirectory) {
                        cacheDir.deleteRecursively()
                        cacheDir.mkdirs()
                    }
                }

                context.imageCacheDataStore.edit { preferences ->
                    preferences[KEY_LAST_CLEANUP] = System.currentTimeMillis()
                    preferences[KEY_LAST_CLEANUP_SIZE] = 0L
                }

                Timber.d("Image cache cleared successfully")
            } catch (e: Exception) {
                Timber.e(e, "Failed to clear image cache")
            }
        }
    }

    @Suppress("unused")
    suspend fun clearDiskCache(context: Context) {
        withContext(Dispatchers.IO) {
            try {
                val cacheDirs = listOf(
                    File(context.cacheDir, "image_cache"),
                    File(context.cacheDir, "coil3_disk_cache"),
                    File(context.cacheDir, "image_loader_disk_cache")
                )

                cacheDirs.forEach { cacheDir ->
                    if (cacheDir.exists() && cacheDir.isDirectory) {
                        cacheDir.deleteRecursively()
                        cacheDir.mkdirs()
                    }
                }

                Timber.d("Disk cache cleared successfully")
            } catch (e: Exception) {
                Timber.e(e, "Failed to clear disk cache")
            }
        }
    }

    fun getCacheSize(context: Context): Long {
        return try {
            var totalSize = 0L

            val cacheDirs = listOf(
                File(context.cacheDir, "image_cache"),
                File(context.cacheDir, "coil3_disk_cache"),
                File(context.cacheDir, "image_loader_disk_cache"),
                context.cacheDir
            )

            val countedFiles = mutableSetOf<String>()

            cacheDirs.forEach { dir ->
                if (dir.exists() && dir.isDirectory) {
                    var dirSize = 0L
                    var dirFiles = 0

                    dir.walkTopDown()
                        .filter { it.isFile }
                        .forEach { file ->
                            val path = file.absolutePath
                            if (!countedFiles.contains(path) &&
                                (path.contains("image") ||
                                 path.contains("coil") ||
                                 path.contains(".cache") ||
                                 path.contains("disk_cache"))) {
                                countedFiles.add(path)
                                val size = file.length()
                                totalSize += size
                                dirSize += size
                                dirFiles++
                            }
                        }

                    if (dirFiles > 0) {
                        Timber.d("Cache dir: ${dir.name}, size: ${formatSize(dirSize)}, files: $dirFiles")
                    }
                }
            }

            Timber.d("Cache size calculated: ${formatSize(totalSize)}, files: ${countedFiles.size}")
            totalSize
        } catch (e: Exception) {
            Timber.e(e, "Failed to calculate cache size")
            0L
        }
    }

    @Suppress("unused")
    suspend fun getLastCleanupInfo(context: Context): CleanupInfo {
        return withContext(Dispatchers.IO) {
            val preferences = context.imageCacheDataStore.data.first()
            val lastCleanup = preferences[KEY_LAST_CLEANUP] ?: 0L
            val lastSize = preferences[KEY_LAST_CLEANUP_SIZE] ?: 0L
            val currentSize = getCacheSize(context)

            CleanupInfo(
                lastCleanupTime = lastCleanup,
                lastCleanupSize = lastSize,
                currentSize = currentSize,
                daysSinceCleanup = if (lastCleanup > 0) {
                    ((System.currentTimeMillis() - lastCleanup) / (1000 * 60 * 60 * 24)).toInt()
                } else {
                    -1
                }
            )
        }
    }

    fun formatSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            else -> String.format(Locale.US, "%.2f MB", bytes / (1024.0 * 1024.0))
        }
    }

    data class CleanupInfo(
        val lastCleanupTime: Long,
        val lastCleanupSize: Long,
        val currentSize: Long,
        val daysSinceCleanup: Int
    )
}