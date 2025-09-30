package com.koniukhov.cinecircle.network

import android.content.Context
import com.koniukhov.cinecircle.core.data.util.NetworkUtils
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkStatusProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkStatusProvider {
    override fun isNetworkAvailable(): Boolean = NetworkUtils.isNetworkAvailable(context)
}