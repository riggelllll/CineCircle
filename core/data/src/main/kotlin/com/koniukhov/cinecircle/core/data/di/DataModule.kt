package com.koniukhov.cinecircle.core.data.di

import com.koniukhov.cinecircle.core.data.remote.RemoteCollectionsDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteGenresDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteImagesDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteMovieCreditsDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteMovieReviewsDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteVideosDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteMoviesDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteReleaseDatesDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteTvSeriesDataSource
import com.koniukhov.cinecircle.core.data.repository.CollectionsRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.CreditsRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.GenresRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.ImagesRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.MoviesRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.ReleaseDatesRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.ReviewsRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.TvSeriesRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.VideosRepositoryImpl
import com.koniukhov.cinecircle.core.domain.datasource.CollectionsDataSource
import com.koniukhov.cinecircle.core.domain.datasource.CreditsDatasource
import com.koniukhov.cinecircle.core.domain.datasource.GenresDataSource
import com.koniukhov.cinecircle.core.domain.datasource.ImagesDataSource
import com.koniukhov.cinecircle.core.domain.datasource.MoviesDataSource
import com.koniukhov.cinecircle.core.domain.datasource.ReleaseDatesDataSource
import com.koniukhov.cinecircle.core.domain.datasource.ReviewsDataSource
import com.koniukhov.cinecircle.core.domain.datasource.TvSeriesDataSource
import com.koniukhov.cinecircle.core.domain.datasource.VideosDataSource
import com.koniukhov.cinecircle.core.domain.repository.CollectionsRepository
import com.koniukhov.cinecircle.core.domain.repository.CreditsRepository
import com.koniukhov.cinecircle.core.domain.repository.GenresRepository
import com.koniukhov.cinecircle.core.domain.repository.ImagesRepository
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import com.koniukhov.cinecircle.core.domain.repository.ReleaseDatesRepository
import com.koniukhov.cinecircle.core.domain.repository.ReviewsRepository
import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import com.koniukhov.cinecircle.core.domain.repository.VideosRepository
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
    abstract fun bindGenresDataSource(
        remoteGenresDataSource: RemoteGenresDataSource
    ): GenresDataSource

    @Binds
    @Singleton
    abstract fun bindGenresRepository(
        genresRepository: GenresRepositoryImpl
    ): GenresRepository

    @Binds
    @Singleton
    abstract fun bindCollectionsDataSource(
        remoteCollectionsDataSource: RemoteCollectionsDataSource
    ): CollectionsDataSource

    @Binds
    @Singleton
    abstract fun bindCollectionsRepository(
        collectionsRepository: CollectionsRepositoryImpl
    ): CollectionsRepository

    @Binds
    @Singleton
    abstract fun bindImageDataSource(
        remoteImageDataSource: RemoteImagesDataSource
    ): ImagesDataSource

    @Binds
    @Singleton
    abstract fun bindImagesRepository(
        imagesRepository: ImagesRepositoryImpl
    ): ImagesRepository

    @Binds
    @Singleton
    abstract fun bindVideosDataSource(
        remoteVideosDataSource: RemoteVideosDataSource
    ): VideosDataSource

    @Binds
    @Singleton
    abstract fun bindVideosRepository(
        videosRepository: VideosRepositoryImpl
    ): VideosRepository

    @Binds
    @Singleton
    abstract fun bindReviewsDataSource(
        remoteMovieReviewsDataSource: RemoteMovieReviewsDataSource
    ): ReviewsDataSource

    @Binds
    @Singleton
    abstract fun bindReviewsRepository(
        reviewsRepository: ReviewsRepositoryImpl
    ): ReviewsRepository

    @Binds
    @Singleton
    abstract fun bindCreditsDataSource(
        remoteMovieCreditsDataSource: RemoteMovieCreditsDataSource
    ): CreditsDatasource

    @Binds
    @Singleton
    abstract fun bindCreditsRepository(
        creditsRepositoryImpl: CreditsRepositoryImpl
    ): CreditsRepository

    @Binds
    @Singleton
    abstract fun bindReleaseDatesDataSource(
        remoteReleaseDatesDataSource: RemoteReleaseDatesDataSource
    ): ReleaseDatesDataSource

    @Binds
    @Singleton
    abstract fun bindReleaseDatesRepository(
        releaseDatesRepositoryImpl: ReleaseDatesRepositoryImpl
    ): ReleaseDatesRepository
}