package com.koniukhov.cinecirclex.core.domain

interface NetworkStatusProvider {
    fun isNetworkAvailable(): Boolean
}