package com.koniukhov.cinecircle.core.domain

interface NetworkStatusProvider {
    fun isNetworkAvailable(): Boolean
}