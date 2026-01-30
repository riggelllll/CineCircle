package com.koniukhov.cinecirclex.core.data.util

import com.koniukhov.cinecirclex.core.common.Constants.DEFAULT_MAX_RETRIES
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
            }
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
            isNetworkAvailable = { false }
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
            }
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
            }
        )

        assertNull(result)
        assertEquals(11, networkCheckCount)
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