package com.koniukhov.cinecircle.core.data.di

import com.koniukhov.cinecircle.core.data.remote.RemoteGenresDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteMoviesDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteTvSeriesDataSource
import com.koniukhov.cinecircle.core.data.repository.GenresRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.MoviesRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.TvSeriesRepositoryImpl
import com.koniukhov.cinecircle.core.domain.datasource.GenresDataSource
import com.koniukhov.cinecircle.core.domain.datasource.MoviesDataSource
import com.koniukhov.cinecircle.core.domain.datasource.TvSeriesDataSource
import com.koniukhov.cinecircle.core.domain.repository.GenresRepository
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindMoviesRepository(
        moviesRepository: MoviesRepositoryImpl
    ): MoviesRepository

    @Binds
    @Singleton
    abstract fun bindMoviesDataSource(
        remoteMovieDataSource: RemoteMoviesDataSource
    ): MoviesDataSource

    @Binds
    @Singleton
    abstract fun bindTvSeriesDataSource(
        remoteTvSeriesDataSource: RemoteTvSeriesDataSource
    ): TvSeriesDataSource

    @Binds
    @Singleton
    abstract fun bindTvSeriesRepository(
        tvSeriesRepository: TvSeriesRepositoryImpl
    ): TvSeriesRepository

    @Binds
    @Singleton
    abstract fun bindRemoteGenresDataSource(
        remoteGenresDataSource: RemoteGenresDataSource
    ): GenresDataSource

    @Binds
    @Singleton
    abstract fun bindGenresRepository(
        genresRepository: GenresRepositoryImpl
    ): GenresRepository
}