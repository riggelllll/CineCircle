package com.koniukhov.cinecirclex.di

import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.network.NetworkStatusProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    @Binds
    abstract fun bindNetworkStatusProvider(
        impl: NetworkStatusProviderImpl
    ): NetworkStatusProvider
}