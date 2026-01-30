package com.koniukhov.cinecirclex.network

import android.content.Context
import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.core.data.util.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkStatusProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkStatusProvider {
    override fun isNetworkAvailable(): Boolean = NetworkUtils.isNetworkAvailable(context)
}