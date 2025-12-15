package com.koniukhov.cinecircle.core.data.util

import com.koniukhov.cinecircle.core.common.Constants.DEFAULT_MAX_RETRIES
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class FetchUtilsTest {

    @Test
    fun `fetchWithLocalAndRetry should return remote data when network is available`() = runTest {
        var remoteCalled = false
        var localCalled = false

        val result = fetchWithLocalAndRetry(
            remoteCall = {
                remoteCalled = true
                "remote_data"
            },
            localCall = {
                localCalled = true
                "local_data"
            },
            isNetworkAvailable = { true }
        )

        assertEquals("remote_data", result)
        assertTrue(remoteCalled)
        assertFalse(localCalled)
    }

    @Test
    fun `fetchWithLocalAndRetry should return local data when remote fails and network is available`() = runTest {
        var remoteCalled = false
        var localCalled = false

        val result = fetchWithLocalAndRetry(
            remoteCall = {
                remoteCalled = true
                throw Exception("Network error")
            },
            localCall = {
                localCalled = true
                "local_data"
            },
            isNetworkAvailable = { true }
        )

        assertEquals("local_data", result)
        assertTrue(remoteCalled)
        assertTrue(localCalled)
    }

    @Test
    fun `fetchWithLocalAndRetry should return local data immediately when network is not available`() = runTest {
        var remoteCalled = false
        var localCalled = false

        val result = fetchWithLocalAndRetry(
            remoteCall = {
                remoteCalled = true
                "remote_data"
            },
            localCall = {
                localCalled = true
                "local_data"
            },
            isNetworkAvailable = { false }
        )

        assertEquals("local_data", result)
        assertFalse(remoteCalled)
        assertTrue(localCalled)
    }

    @Test
    fun `fetchWithLocalAndRetry should retry when network becomes available`() = runTest {
        var remoteCalled = false
        var localCalled = false
        var networkCheckCount = 0
        val networkAvailableAfter = 2

        val result = fetchWithLocalAndRetry(
            remoteCall = {
                remoteCalled = true
                "remote_data"
            },
            localCall = {
                localCalled = true
                null
            },
            isNetworkAvailable = {
                networkCheckCount++
                networkCheckCount > networkAvailableAfter
            },
            maxRetries = 5,
            initialDelay = 10L
        )

        assertEquals("remote_data", result)
        assertTrue(remoteCalled)
        assertTrue(localCalled)
        assertTrue(networkCheckCount >= networkAvailableAfter)
    }

    @Test
    fun `fetchWithLocalAndRetry should return null when no local data and network never available`() = runTest {
        var remoteCalled = false
        var localCalled = false

        val result = fetchWithLocalAndRetry(
            remoteCall = {
                remoteCalled = true
                "remote_data"
            },
            localCall = {
                localCalled = true
                null
            },
            isNetworkAvailable = { false },
            maxRetries = 3,
            initialDelay = 10L
        )

        assertNull(result)
        assertFalse(remoteCalled)
        assertTrue(localCalled)
    }

    @Test
    fun `fetchWithLocalAndRetry should return null when remote fails after retry`() = runTest {
        var remoteCalled = false
        var localCalled = false
        var networkCheckCount = 0

        val result = fetchWithLocalAndRetry(
            remoteCall = {
                remoteCalled = true
                throw Exception("Network error")
            },
            localCall = {
                localCalled = true
                null
            },
            isNetworkAvailable = {
                networkCheckCount++
                networkCheckCount > 2
            },
            maxRetries = 5,
            initialDelay = 10L
        )

        assertNull(result)
        assertTrue(remoteCalled)
        assertTrue(localCalled)
    }

    @Test
    fun `fetchWithLocalAndRetry should handle exception in local call`() = runTest {
        var remoteCalled = false
        var exceptionThrown = false

        try {
            fetchWithLocalAndRetry(
                remoteCall = {
                    remoteCalled = true
                    throw Exception("Network error")
                },
                localCall = {
                    throw Exception("Local error")
                },
                isNetworkAvailable = { true }
            )
        } catch (_: Exception) {
            exceptionThrown = true
        }

        assertTrue(remoteCalled)
        assertTrue(exceptionThrown)
    }

    @Test
    fun `fetchWithLocalAndRetry should delay between retries with exponential backoff`() = runTest {
        var networkCheckCount = 0

        val result = fetchWithLocalAndRetry(
            remoteCall = { "remote_data" },
            localCall = { null },
            isNetworkAvailable = {
                networkCheckCount++
                false
            },
            maxRetries = 3,
            initialDelay = 50L
        )

        assertNull(result)
        assertEquals(4, networkCheckCount)
    }

    @Test
    fun `fetchWithLocalAndRetry should use default retry parameters`() = runTest {
        var retryCount = 0

        val result = fetchWithLocalAndRetry(
            remoteCall = { "remote_data" },
            localCall = { null },
            isNetworkAvailable = {
                retryCount++
                retryCount > DEFAULT_MAX_RETRIES
            }
        )

        assertEquals("remote_data", result)
        assertTrue(retryCount <= DEFAULT_MAX_RETRIES + 1)
    }
}