package com.koniukhov.cinecircle.core.data.di

import com.koniukhov.cinecircle.core.data.remote.RemoteCollectionsDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteContentRatingsDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteGenresDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteImagesDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteCreditsDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteReviewsDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteVideosDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteMoviesDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteReleaseDatesDataSourceImpl
import com.koniukhov.cinecircle.core.data.remote.RemoteTvSeriesDataSourceImpl
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
import com.koniukhov.cinecircle.core.domain.datasource.CollectionsDataSource
import com.koniukhov.cinecircle.core.domain.datasource.ContentRatingsDataSource
import com.koniukhov.cinecircle.core.domain.datasource.CreditsDatasource
import com.koniukhov.cinecircle.core.domain.datasource.GenresDataSource
import com.koniukhov.cinecircle.core.domain.datasource.ImagesDataSource
import com.koniukhov.cinecircle.core.domain.datasource.MoviesDataSource
import com.koniukhov.cinecircle.core.domain.datasource.ReleaseDatesDataSource
import com.koniukhov.cinecircle.core.domain.datasource.ReviewsDataSource
import com.koniukhov.cinecircle.core.domain.datasource.TvSeriesDataSource
import com.koniukhov.cinecircle.core.domain.datasource.VideosDataSource
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
    abstract fun bindMoviesDataSource(
        remoteMovieDataSource: RemoteMoviesDataSourceImpl
    ): MoviesDataSource

    @Binds
    @Singleton
    abstract fun bindTvSeriesDataSource(
        remoteTvSeriesDataSourceImpl: RemoteTvSeriesDataSourceImpl
    ): TvSeriesDataSource

    @Binds
    @Singleton
    abstract fun bindTvSeriesRepository(
        tvSeriesRepository: TvSeriesRepositoryImpl
    ): TvSeriesRepository

    @Binds
    @Singleton
    abstract fun bindGenresDataSource(
        remoteGenresDataSourceImpl: RemoteGenresDataSourceImpl
    ): GenresDataSource

    @Binds
    @Singleton
    abstract fun bindGenresRepository(
        genresRepository: GenresRepositoryImpl
    ): GenresRepository

    @Binds
    @Singleton
    abstract fun bindCollectionsDataSource(
        remoteCollectionsDataSourceImpl: RemoteCollectionsDataSourceImpl
    ): CollectionsDataSource

    @Binds
    @Singleton
    abstract fun bindCollectionsRepository(
        collectionsRepository: CollectionsRepositoryImpl
    ): CollectionsRepository

    @Binds
    @Singleton
    abstract fun bindImageDataSource(
        remoteImageDataSource: RemoteImagesDataSourceImpl
    ): ImagesDataSource

    @Binds
    @Singleton
    abstract fun bindImagesRepository(
        imagesRepository: ImagesRepositoryImpl
    ): ImagesRepository

    @Binds
    @Singleton
    abstract fun bindVideosDataSource(
        remoteVideosDataSourceImpl: RemoteVideosDataSourceImpl
    ): VideosDataSource

    @Binds
    @Singleton
    abstract fun bindVideosRepository(
        videosRepository: VideosRepositoryImpl
    ): VideosRepository

    @Binds
    @Singleton
    abstract fun bindReviewsDataSource(
        remoteReviewsDataSourceImpl: RemoteReviewsDataSourceImpl
    ): ReviewsDataSource

    @Binds
    @Singleton
    abstract fun bindReviewsRepository(
        reviewsRepository: ReviewsRepositoryImpl
    ): ReviewsRepository

    @Binds
    @Singleton
    abstract fun bindCreditsDataSource(
        remoteCreditsDataSourceImpl: RemoteCreditsDataSourceImpl
    ): CreditsDatasource

    @Binds
    @Singleton
    abstract fun bindCreditsRepository(
        creditsRepositoryImpl: CreditsRepositoryImpl
    ): CreditsRepository

    @Binds
    @Singleton
    abstract fun bindReleaseDatesDataSource(
        remoteReleaseDatesDataSourceImpl: RemoteReleaseDatesDataSourceImpl
    ): ReleaseDatesDataSource

    @Binds
    @Singleton
    abstract fun bindReleaseDatesRepository(
        releaseDatesRepositoryImpl: ReleaseDatesRepositoryImpl
    ): ReleaseDatesRepository

    @Binds
    @Singleton
    abstract fun bindContentRatingsDataSource(
        remoteContentRatingsDataSourceImpl: RemoteContentRatingsDataSourceImpl
    ): ContentRatingsDataSource

    @Binds
    @Singleton
    abstract fun bindContentRatingsRepository(
        contentRatingsRepositoryImpl: ContentRatingsRepositoryImpl
    ): ContentRatingsRepository
}