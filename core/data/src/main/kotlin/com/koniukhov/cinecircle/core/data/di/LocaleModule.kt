package com.koniukhov.cinecircle.core.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocaleModule {

    @Provides
    @Singleton
    @LanguageCode
    fun provideLanguageCode(@ApplicationContext context: Context): String {
        return context.resources.configuration.locales[0].language
    }

    @Provides
    @Singleton
    @CountryCode
    fun provideCountryCode(@ApplicationContext context: Context): String {
        return context.resources.configuration.locales[0].country
    }
}