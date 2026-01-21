package com.koniukhov.cinecircle.core.data.util

import com.koniukhov.cinecircle.core.common.Constants.NETWORK_CHECK_INTERVAL_MS
import com.koniukhov.cinecircle.core.common.Constants.NETWORK_WAIT_TIMEOUT_MS
import kotlinx.coroutines.delay
import timber.log.Timber

suspend fun <T> fetchWithLocalAndRetry(
    remoteCall: suspend () -> T,
    localCall: suspend () -> T?,
    isNetworkAvailable: () -> Boolean
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

        var waited = 0L
        while (waited < NETWORK_WAIT_TIMEOUT_MS) {
            delay(NETWORK_CHECK_INTERVAL_MS)
            waited += NETWORK_CHECK_INTERVAL_MS

            if (isNetworkAvailable()) {
                return try {
                    remoteCall()
                } catch (e: Exception) {
                    Timber.d(e)
                    null
                }
            }
        }
        return null
    }
}