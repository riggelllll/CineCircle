package com.koniukhov.cinecirclex.core.data.di

import com.koniukhov.cinecirclex.core.data.local.LocalMoviesDataSource
import com.koniukhov.cinecirclex.core.data.local.LocalMoviesDataSourceImpl
import com.koniukhov.cinecirclex.core.data.local.LocalTvSeriesDataSource
import com.koniukhov.cinecirclex.core.data.local.LocalTvSeriesDataSourceImpl
import com.koniukhov.cinecirclex.core.data.remote.RemoteCollectionsDataSourceImpl
import com.koniukhov.cinecirclex.core.data.remote.RemoteContentRatingsDataSourceImpl
import com.koniukhov.cinecirclex.core.data.remote.RemoteGenresDataSourceImpl
import com.koniukhov.cinecirclex.core.data.remote.RemoteImagesDataSourceImpl
import com.koniukhov.cinecirclex.core.data.remote.RemoteCreditsDataSourceImpl
import com.koniukhov.cinecirclex.core.data.remote.RemoteReviewsDataSourceImpl
import com.koniukhov.cinecirclex.core.data.remote.RemoteVideosDataSourceImpl
import com.koniukhov.cinecirclex.core.data.remote.RemoteMoviesDataSourceImpl
import com.koniukhov.cinecirclex.core.data.remote.RemoteReleaseDatesDataSourceImpl
import com.koniukhov.cinecirclex.core.data.remote.RemoteTvSeriesDataSourceImpl
import com.koniukhov.cinecirclex.core.data.repository.CollectionsRepositoryImpl
import com.koniukhov.cinecirclex.core.data.repository.ContentRatingsRepositoryImpl
import com.koniukhov.cinecirclex.core.data.repository.CreditsRepositoryImpl
import com.koniukhov.cinecirclex.core.data.repository.GenresRepositoryImpl
import com.koniukhov.cinecirclex.core.data.repository.ImagesRepositoryImpl
import com.koniukhov.cinecirclex.core.data.repository.MoviesRepositoryImpl
import com.koniukhov.cinecirclex.core.data.repository.ReleaseDatesRepositoryImpl
import com.koniukhov.cinecirclex.core.data.repository.ReviewsRepositoryImpl
import com.koniukhov.cinecirclex.core.data.repository.TvSeriesRepositoryImpl
import com.koniukhov.cinecirclex.core.data.repository.VideosRepositoryImpl
import com.koniukhov.cinecirclex.core.data.remote.RemoteCollectionsDataSource
import com.koniukhov.cinecirclex.core.data.remote.RemoteContentRatingsDataSource
import com.koniukhov.cinecirclex.core.data.remote.RemoteCreditsDatasource
import com.koniukhov.cinecirclex.core.data.remote.RemoteGenresDataSource
import com.koniukhov.cinecirclex.core.data.remote.RemoteImagesDataSource
import com.koniukhov.cinecirclex.core.data.remote.RemoteMoviesDataSource
import com.koniukhov.cinecirclex.core.data.remote.RemoteReleaseDatesDataSource
import com.koniukhov.cinecirclex.core.data.remote.RemoteReviewsDataSource
import com.koniukhov.cinecirclex.core.data.remote.RemoteTvSeriesDataSource
import com.koniukhov.cinecirclex.core.data.remote.RemoteVideosDataSource
import com.koniukhov.cinecirclex.core.domain.repository.CollectionsRepository
import com.koniukhov.cinecirclex.core.domain.repository.ContentRatingsRepository
import com.koniukhov.cinecirclex.core.domain.repository.CreditsRepository
import com.koniukhov.cinecirclex.core.domain.repository.GenresRepository
import com.koniukhov.cinecirclex.core.domain.repository.ImagesRepository
import com.koniukhov.cinecirclex.core.domain.repository.MoviesRepository
import com.koniukhov.cinecirclex.core.domain.repository.ReleaseDatesRepository
import com.koniukhov.cinecirclex.core.domain.repository.ReviewsRepository
import com.koniukhov.cinecirclex.core.domain.repository.TvSeriesRepository
import com.koniukhov.cinecirclex.core.domain.repository.VideosRepository
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
        remoteMovieDataSource: RemoteMoviesDataSourceImpl
    ): RemoteMoviesDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteTvSeriesDataSource(
        remoteTvSeriesDataSourceImpl: RemoteTvSeriesDataSourceImpl
    ): RemoteTvSeriesDataSource

    @Binds
    @Singleton
    abstract fun bindTvSeriesRepository(
        tvSeriesRepository: TvSeriesRepositoryImpl
    ): TvSeriesRepository

    @Binds
    @Singleton
    abstract fun bindRemoteGenresDataSource(
        remoteGenresDataSourceImpl: RemoteGenresDataSourceImpl
    ): RemoteGenresDataSource

    @Binds
    @Singleton
    abstract fun bindGenresRepository(
        genresRepository: GenresRepositoryImpl
    ): GenresRepository

    @Binds
    @Singleton
    abstract fun bindRemoteCollectionsDataSource(
        remoteCollectionsDataSourceImpl: RemoteCollectionsDataSourceImpl
    ): RemoteCollectionsDataSource

    @Binds
    @Singleton
    abstract fun bindCollectionsRepository(
        collectionsRepository: CollectionsRepositoryImpl
    ): CollectionsRepository

    @Binds
    @Singleton
    abstract fun bindRemoteImageDataSource(
        remoteImageDataSource: RemoteImagesDataSourceImpl
    ): RemoteImagesDataSource

    @Binds
    @Singleton
    abstract fun bindImagesRepository(
        imagesRepository: ImagesRepositoryImpl
    ): ImagesRepository

    @Binds
    @Singleton
    abstract fun bindRemoteVideosDataSource(
        remoteVideosDataSourceImpl: RemoteVideosDataSourceImpl
    ): RemoteVideosDataSource

    @Binds
    @Singleton
    abstract fun bindVideosRepository(
        videosRepository: VideosRepositoryImpl
    ): VideosRepository

    @Binds
    @Singleton
    abstract fun bindRemoteReviewsDataSource(
        remoteReviewsDataSourceImpl: RemoteReviewsDataSourceImpl
    ): RemoteReviewsDataSource

    @Binds
    @Singleton
    abstract fun bindReviewsRepository(
        reviewsRepository: ReviewsRepositoryImpl
    ): ReviewsRepository

    @Binds
    @Singleton
    abstract fun bindRemoteCreditsDataSource(
        remoteCreditsDataSourceImpl: RemoteCreditsDataSourceImpl
    ): RemoteCreditsDatasource

    @Binds
    @Singleton
    abstract fun bindCreditsRepository(
        creditsRepositoryImpl: CreditsRepositoryImpl
    ): CreditsRepository

    @Binds
    @Singleton
    abstract fun bindRemoteReleaseDatesDataSource(
        remoteReleaseDatesDataSourceImpl: RemoteReleaseDatesDataSourceImpl
    ): RemoteReleaseDatesDataSource

    @Binds
    @Singleton
    abstract fun bindReleaseDatesRepository(
        releaseDatesRepositoryImpl: ReleaseDatesRepositoryImpl
    ): ReleaseDatesRepository

    @Binds
    @Singleton
    abstract fun bindRemoteContentRatingsDataSource(
        remoteContentRatingsDataSourceImpl: RemoteContentRatingsDataSourceImpl
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

    @Binds
    @Singleton
    abstract fun bindLocalTvSeriesDataSource(
        localTvSeriesDataSourceImpl: LocalTvSeriesDataSourceImpl
    ): LocalTvSeriesDataSource
}