package com.koniukhov.cinecircle.core.data.di

import com.koniukhov.cinecircle.core.data.local.LocalMoviesDataSource
import com.koniukhov.cinecircle.core.data.local.LocalMoviesDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteCollectionsDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteContentRatingsDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteGenresDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteImagesDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteCreditsDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteReviewsDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteVideosDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteMoviesDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteReleaseDatesDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteTvSeriesDataSourceImpl
import com.koniukhov.cinecircle.core.data.repository.CollectionsRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.ContentRatingsRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.CreditsRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.GenresRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.ImagesRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.MoviesRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.ReleaseDatesRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.ReviewsRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.TvSeriesRepositoryImpl
import com.koniukhov.cinecircle.core.data.repository.VideosRepositoryImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteCollectionsDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteContentRatingsDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteCreditsDatasource
import com.koniukhov.cinecircle.core.data.remote.RemoteGenresDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteImagesDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteMoviesDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteReleaseDatesDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteReviewsDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteTvSeriesDataSource
import com.koniukhov.cinecircle.core.data.remote.RemoteVideosDataSource
import com.koniukhov.cinecircle.core.domain.repository.CollectionsRepository
import com.koniukhov.cinecircle.core.domain.repository.ContentRatingsRepository
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
    abstract fun bindRemoteMoviesDataSource(
        remoteMovieDataSource: RemoteRemoteMoviesDataSourceImpl
    ): RemoteMoviesDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteTvSeriesDataSource(
        remoteTvSeriesDataSourceImpl: RemoteRemoteTvSeriesDataSourceImpl
    ): RemoteTvSeriesDataSource

    @Binds
    @Singleton
    abstract fun bindTvSeriesRepository(
        tvSeriesRepository: TvSeriesRepositoryImpl
    ): TvSeriesRepository

    @Binds
    @Singleton
    abstract fun bindRemoteGenresDataSource(
        remoteGenresDataSourceImpl: RemoteRemoteGenresDataSourceImpl
    ): RemoteGenresDataSource

    @Binds
    @Singleton
    abstract fun bindGenresRepository(
        genresRepository: GenresRepositoryImpl
    ): GenresRepository

    @Binds
    @Singleton
    abstract fun bindRemoteCollectionsDataSource(
        remoteCollectionsDataSourceImpl: RemoteRemoteCollectionsDataSourceImpl
    ): RemoteCollectionsDataSource

    @Binds
    @Singleton
    abstract fun bindCollectionsRepository(
        collectionsRepository: CollectionsRepositoryImpl
    ): CollectionsRepository

    @Binds
    @Singleton
    abstract fun bindRemoteImageDataSource(
        remoteImageDataSource: RemoteRemoteImagesDataSourceImpl
    ): RemoteImagesDataSource

    @Binds
    @Singleton
    abstract fun bindImagesRepository(
        imagesRepository: ImagesRepositoryImpl
    ): ImagesRepository

    @Binds
    @Singleton
    abstract fun bindRemoteVideosDataSource(
        remoteVideosDataSourceImpl: RemoteRemoteVideosDataSourceImpl
    ): RemoteVideosDataSource

    @Binds
    @Singleton
    abstract fun bindVideosRepository(
        videosRepository: VideosRepositoryImpl
    ): VideosRepository

    @Binds
    @Singleton
    abstract fun bindRemoteReviewsDataSource(
        remoteReviewsDataSourceImpl: RemoteRemoteReviewsDataSourceImpl
    ): RemoteReviewsDataSource

    @Binds
    @Singleton
    abstract fun bindReviewsRepository(
        reviewsRepository: ReviewsRepositoryImpl
    ): ReviewsRepository

    @Binds
    @Singleton
    abstract fun bindRemoteCreditsDataSource(
        remoteCreditsDataSourceImpl: RemoteRemoteCreditsDataSourceImpl
    ): RemoteCreditsDatasource

    @Binds
    @Singleton
    abstract fun bindCreditsRepository(
        creditsRepositoryImpl: CreditsRepositoryImpl
    ): CreditsRepository

    @Binds
    @Singleton
    abstract fun bindRemoteReleaseDatesDataSource(
        remoteReleaseDatesDataSourceImpl: RemoteRemoteReleaseDatesDataSourceImpl
    ): RemoteReleaseDatesDataSource

    @Binds
    @Singleton
    abstract fun bindReleaseDatesRepository(
        releaseDatesRepositoryImpl: ReleaseDatesRepositoryImpl
    ): ReleaseDatesRepository

    @Binds
    @Singleton
    abstract fun bindRemoteContentRatingsDataSource(
        remoteContentRatingsDataSourceImpl: RemoteRemoteContentRatingsDataSourceImpl
    ): RemoteContentRatingsDataSource

    @Binds
    @Singleton
    abstract fun bindContentRatingsRepository(
        contentRatingsRepositoryImpl: ContentRatingsRepositoryImpl
    ): ContentRatingsRepository

    @Binds
    @Singleton
    abstract fun bindLocalMoviesDataSource(
        localMoviesDataSourceImpl: LocalMoviesDataSourceImpl
    ): LocalMoviesDataSource
}