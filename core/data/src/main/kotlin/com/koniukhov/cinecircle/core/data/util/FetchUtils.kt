package com.koniukhov.cinecircle.core.data.util

import com.koniukhov.cinecircle.core.common.Constants.DEFAULT_INITIAL_DELAY_MS
import com.koniukhov.cinecircle.core.common.Constants.DEFAULT_MAX_RETRIES
import com.koniukhov.cinecircle.core.common.Constants.RETRY_DELAY_MULTIPLIER
import kotlinx.coroutines.delay
import timber.log.Timber

suspend fun <T> fetchWithLocalAndRetry(
    remoteCall: suspend () -> T,
    localCall: suspend () -> T?,
    isNetworkAvailable: () -> Boolean,
    maxRetries: Int = DEFAULT_MAX_RETRIES,
    initialDelay: Long = DEFAULT_INITIAL_DELAY_MS
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
            currentDelay *= RETRY_DELAY_MULTIPLIER
        }
        return null
    }
}