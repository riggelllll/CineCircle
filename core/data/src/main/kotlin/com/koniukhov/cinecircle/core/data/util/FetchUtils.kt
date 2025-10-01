package com.koniukhov.cinecircle.core.data.util

import kotlinx.coroutines.delay
import timber.log.Timber

suspend fun <T> fetchWithLocalAndRetry(
    remoteCall: suspend () -> T,
    localCall: suspend () -> T?,
    isNetworkAvailable: () -> Boolean,
    maxRetries: Int = 5,
    initialDelay: Long = 1000L
): T? {
    if (isNetworkAvailable()) {
        return try {
            remoteCall()
        } catch (e: Exception) {
            Timber.d(e)
            localCall()
        }
    } else {
        val local = localCall()
        if (local != null) return local
        var currentDelay = initialDelay
        repeat(maxRetries) {
            delay(currentDelay)
            if (isNetworkAvailable()) {
                return try {
                    remoteCall()
                } catch (e: Exception) {
                    Timber.d(e)
                    null
                }
            }
            currentDelay *= 2
        }
        return null
    }
}