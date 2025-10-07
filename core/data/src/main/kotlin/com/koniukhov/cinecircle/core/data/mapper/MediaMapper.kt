package com.koniukhov.cinecircle.core.data.mapper

import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID
import com.koniukhov.cinecircle.core.database.entity.GenreEntity
import com.koniukhov.cinecircle.core.database.entity.MovieDetailsEntity
import com.koniukhov.cinecircle.core.database.entity.MovieWithGenres
import com.koniukhov.cinecircle.core.database.entity.TvSeriesDetailsEntity
import com.koniukhov.cinecircle.core.database.entity.TvSeriesWithGenres
import com.koniukhov.cinecircle.core.domain.model.CastMember
import com.koniukhov.cinecircle.core.domain.model.CollectionDetails
import com.koniukhov.cinecircle.core.domain.model.CollectionMedia
import com.koniukhov.cinecircle.core.domain.model.ContentRating
import com.koniukhov.cinecircle.core.domain.model.Creator
import com.koniukhov.cinecircle.core.domain.model.CrewMember
import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.model.Image
import com.koniukhov.cinecircle.core.domain.model.Language
import com.koniukhov.cinecircle.core.domain.model.MediaCredits
import com.koniukhov.cinecircle.core.domain.model.MediaImages
import com.koniukhov.cinecircle.core.domain.model.MediaReview
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.MovieCollection
import com.koniukhov.cinecircle.core.domain.model.MovieDetails
import com.koniukhov.cinecircle.core.domain.model.MediaVideos
import com.koniukhov.cinecircle.core.domain.model.Network
import com.koniukhov.cinecircle.core.domain.model.ProductionCompany
import com.koniukhov.cinecircle.core.domain.model.ProductionCountry
import com.koniukhov.cinecircle.core.domain.model.ReleaseDate
import com.koniukhov.cinecircle.core.domain.model.ReleaseDateResult
import com.koniukhov.cinecircle.core.domain.model.ReviewAuthor
import com.koniukhov.cinecircle.core.domain.model.TvEpisodeDetails
import com.koniukhov.cinecircle.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.model.TvSeriesDetails
import com.koniukhov.cinecircle.core.domain.model.Video
import com.koniukhov.cinecircle.core.network.model.CastMemberDto
import com.koniukhov.cinecircle.core.network.model.CollectionDetailsDto
import com.koniukhov.cinecircle.core.network.model.CollectionMediaDto
import com.koniukhov.cinecircle.core.network.model.ContentRatingDto
import com.koniukhov.cinecircle.core.network.model.CreatorDto
import com.koniukhov.cinecircle.core.network.model.CrewMemberDto
import com.koniukhov.cinecircle.core.network.model.GenreDto
import com.koniukhov.cinecircle.core.network.model.ImageDto
import com.koniukhov.cinecircle.core.network.model.LanguageDto
import com.koniukhov.cinecircle.core.network.model.MediaCreditsDto
import com.koniukhov.cinecircle.core.network.model.MediaImagesDto
import com.koniukhov.cinecircle.core.network.model.MediaReviewDto
import com.koniukhov.cinecircle.core.network.model.MovieCollectionDto
import com.koniukhov.cinecircle.core.network.model.MovieDetailsDto
import com.koniukhov.cinecircle.core.network.model.MovieDto
import com.koniukhov.cinecircle.core.network.model.MediaVideosDto
import com.koniukhov.cinecircle.core.network.model.NetworkDto
import com.koniukhov.cinecircle.core.network.model.ProductionCompanyDto
import com.koniukhov.cinecircle.core.network.model.ProductionCountryDto
import com.koniukhov.cinecircle.core.network.model.ReleaseDatesDto
import com.koniukhov.cinecircle.core.network.model.ReleaseDatesResultDto
import com.koniukhov.cinecircle.core.network.model.ReviewAuthorDto
import com.koniukhov.cinecircle.core.network.model.TvEpisodeDetailsDto
import com.koniukhov.cinecircle.core.network.model.TvSeasonDetailsDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesDetailsDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesDto
import com.koniukhov.cinecircle.core.network.model.VideoDto

fun MovieDto.toDomain(): Movie = Movie(
    adult = adult ?: false,
    backdropPath = backdropPath ?: "",
    genreIds = genreIds ?: emptyList(),
    id = id ?: 0,
    originalLanguage = originalLanguage ?: "",
    originalTitle = originalTitle ?: "",
    overview = overview ?: "",
    popularity = popularity ?: 0.0f,
    posterPath = posterPath ?: "",
    releaseDate = releaseDate ?: "",
    title = title ?: "",
    video = video ?: false,
    voteAverage = voteAverage ?: 0.0f,
    voteCount = voteCount ?: 0
)

fun TvSeriesDto.toDomain(): TvSeries = TvSeries(
    adult = adult ?: false,
    backdropPath = backdropPath ?: "",
    genreIds = genreIds ?: emptyList(),
    id = id ?: 0,
    originCountry = originCountry ?: emptyList(),
    originalLanguage = originalLanguage ?: "",
    originalName = originalName ?: "",
    overview = overview ?: "",
    popularity = popularity ?: 0.0f,
    posterPath = posterPath ?: "",
    firstAirDate = firstAirDate ?: "",
    title = name ?: "",
    voteAverage = voteAverage ?: 0.0f,
    voteCount = voteCount ?: 0
)

fun GenreDto.toDomain(): Genre =
    Genre(
        id = id ?: 0,
        name = name ?: ""
)

fun MovieDetailsDto.toDomain(): MovieDetails = MovieDetails(
    adult = adult ?: false,
    backdropPath = backdropPath ?: "",
    belongsToCollection = belongsToCollection?.toDomain() ?: MovieCollection(INVALID_ID, "", "", ""),
    budget = budget ?: 0,
    genres = genres?.map { it.toDomain() } ?: emptyList(),
    homePage = homePage ?: "",
    id = id ?: 0,
    imdbId = imdbId ?: "",
    originalLanguage = originalLanguage ?: "",
    originalTitle = originalTitle ?: "",
    overview = overview ?: "",
    popularity = popularity ?: 0.0f,
    posterPath = posterPath ?: "",
    productionCompanies = productionCompanies?.map { it.toDomain() } ?: emptyList(),
    productionCountries = productionCountries?.map { it.toDomain() } ?: emptyList(),
    releaseDate = releaseDate ?: "",
    revenue = revenue ?: 0,
    runtime = runtime ?: 0,
    spokenLanguages = spokenLanguages?.map { it.toDomain() } ?: emptyList(),
    status = status ?: "",
    tagline = tagline ?: "",
    title = title ?: "",
    video = video ?: false,
    voteAverage = voteAverage ?: 0.0f,
    voteCount = voteCount ?: 0
)

fun ProductionCompanyDto.toDomain(): ProductionCompany = ProductionCompany(
    id = id ?: 0,
    logoPath = logoPath ?: "",
    name = name ?: "",
    originCountry = originCountry ?: ""
)

fun ProductionCountryDto.toDomain(): ProductionCountry = ProductionCountry(
    isoName = isoName ?: "",
    name = name ?: ""
)

fun LanguageDto.toDomain(): Language = Language(
    englishName = englishName ?: "",
    isoName = isoName ?: "",
    name = name ?: ""
)

fun MovieCollectionDto.toDomain(): MovieCollection =
    MovieCollection(
        id = id ?: INVALID_ID,
        name = name ?: "",
        posterPath = posterPath ?: "",
        backdropPath = backdropPath ?: ""
)

fun CollectionMediaDto.toDomain(): CollectionMedia = CollectionMedia(
    adult = adult ?: false,
    backdropPath = backdropPath ?: "",
    id = id ?: 0,
    title = title ?: "",
    originalLanguage = originalLanguage ?: "",
    originalTitle = originalTitle ?: "",
    overview = overview ?: "",
    posterPath = posterPath ?: "",
    mediaType = mediaType ?: "",
    genreIds = genreIds ?: emptyList(),
    popularity = popularity ?: 0.0f,
    releaseDate = releaseDate ?: "",
    video = video ?: false,
    voteAverage = voteAverage ?: 0.0f,
    voteCount = voteCount ?: 0
)

fun CollectionDetailsDto.toDomain(): CollectionDetails = CollectionDetails(
    id = id ?: 0,
    name = name ?: "",
    overview = overview ?: "",
    posterPath = posterPath ?: "",
    backdropPath = backdropPath ?: "",
    parts = parts?.map { it.toDomain() } ?: emptyList()
)

fun ImageDto.toDomain(): Image = Image(
    aspectRatio = aspectRatio ?: 0.0,
    filePath = filePath ?: "",
    height = height ?: 0,
    countryCode = countryCode ?: "",
    voteAverage = voteAverage ?: 0.0,
    voteCount = voteCount ?: 0,
    width = width ?: 0
)

fun MediaImagesDto.toDomain(): MediaImages = MediaImages(
    id = id ?: 0,
    backdrops = backdrops?.map { it.toDomain() } ?: emptyList(),
    posters = posters?.map { it.toDomain() } ?: emptyList(),
    logos = logos?.map { it.toDomain() } ?: emptyList()
)

fun VideoDto.toDomain(): Video = Video(
    languageCode = languageCode ?: "",
    countryCode = countryCode ?: "",
    name = name ?: "",
    key = key ?: "",
    site = site ?: "",
    size = size ?: 0,
    type = type ?: "",
    official = official ?: false,
    publishedAt = publishedAt ?: "",
    id = id ?: ""
)

fun MediaVideosDto.toDomain(): MediaVideos = MediaVideos(
    id = id ?: 0,
    results = results?.map { it.toDomain() } ?: emptyList()
)

fun ReviewAuthorDto.toDomain(): ReviewAuthor = ReviewAuthor(
    name = name ?: "",
    username = username ?: "",
    avatarPath = avatarPath ?: "",
    rating = rating ?: ""
)

fun MediaReviewDto.toDomain(): MediaReview = MediaReview(
    author = author ?: "",
    authorDetails = authorDetails?.toDomain() ?: ReviewAuthor.empty(),
    content = content ?: "",
    createdAt = createdAt ?: "",
    id = id ?: "",
    updatedAt = updatedAt ?: "",
    url = url ?: ""
)

fun CastMemberDto.toDomain(): CastMember = CastMember(
    adult = adult ?: false,
    gender = gender ?: 0,
    id = id ?: 0,
    knownForDepartment = knownForDepartment ?: "",
    name = name ?: "",
    originalName = originalName ?: "",
    popularity = popularity ?: 0.0f,
    profilePath = profilePath ?: "",
    castId = castId ?: 0,
    character = character ?: "",
    creditId = creditId ?: "",
    order = order ?: 0
)

fun CrewMemberDto.toDomain(): CrewMember = CrewMember(
    adult = adult ?: false,
    gender = gender ?: 0,
    id = id ?: 0,
    knownForDepartment = knownForDepartment ?: "",
    name = name ?: "",
    originalName = originalName ?: "",
    popularity = popularity ?: 0.0,
    profilePath = profilePath ?: "",
    creditId = creditId ?: "",
    department = department ?: "",
    job = job ?: ""
)

fun MediaCreditsDto.toDomain(): MediaCredits = MediaCredits(
    cast = cast?.map { it.toDomain() } ?: emptyList(),
    crew = crew?.map { it.toDomain() } ?: emptyList()
)

fun ReleaseDatesDto.toDomain(): ReleaseDate = ReleaseDate(
    certification = certification ?: "",
    descriptors = descriptors ?: emptyList(),
    languageCode = languageCode ?: "",
    note = note ?: "",
    releaseDate = releaseDate ?: "",
    releaseType = type ?: 0
)

fun ReleaseDatesResultDto.toDomain(): ReleaseDateResult = ReleaseDateResult(
    countryCode = countryCode ?: "",
    releaseDates = releaseDates?.map { it.toDomain() } ?: emptyList()
)

fun CreatorDto.toDomain(): Creator = Creator(
    id = id ?: INVALID_ID,
    creditId = creditId ?: "",
    name = name ?: "",
    gender = gender ?: 0,
    profilePath = profilePath ?: ""
)

fun NetworkDto.toDomain(): Network = Network(
    name = name ?: "",
    id = id ?: INVALID_ID,
    logoPath = logoPath ?: "",
    originCountry = originCountry ?: ""
)

fun TvEpisodeDetailsDto.toDomain(): TvEpisodeDetails = TvEpisodeDetails(
    airDate = airDate ?: "",
    crew = crew?.map { it.toDomain() } ?: emptyList(),
    episodeNumber = episodeNumber ?: 0,
    guestStars = guestStars?.map { it.toDomain() } ?: emptyList(),
    name = name ?: "",
    overview = overview ?: "",
    id = id ?: INVALID_ID,
    productionCode = productionCode ?: "",
    runtime = runtime ?: 0,
    seasonNumber = seasonNumber ?: 0,
    stillPath = stillPath ?: "",
    voteAverage = voteAverage ?: 0f,
    voteCount = voteCount ?: 0
)

fun TvSeasonDetailsDto.toDomain(): TvSeasonDetails = TvSeasonDetails(
    _id = _id ?: "",
    airDate = airDate ?: "",
    episodes = episodes?.map { it.toDomain() } ?: emptyList(),
    name = name ?: "",
    overview = overview ?: "",
    id = id ?: INVALID_ID,
    posterPath = posterPath ?: "",
    seasonNumber = seasonNumber ?: 0,
    voteAverage = voteAverage ?: 0f
)

fun TvSeriesDetailsDto.toDomain(): TvSeriesDetails = TvSeriesDetails(
    adult = adult ?: false,
    backdropPath = backdropPath ?: "",
    createdBy = createdBy?.map { it.toDomain() } ?: emptyList(),
    episodeRunTime = episodeRunTime ?: emptyList(),
    firstAirDate = firstAirDate ?: "",
    genres = genres?.map { it.toDomain() } ?: emptyList(),
    homepage = homepage ?: "",
    id = id ?: 0,
    inProduction = inProduction ?: false,
    languages = languages ?: emptyList(),
    lastAirDate = lastAirDate ?: "",
    lastEpisodeToAir = lastEpisodeToAir?.toDomain() ?: TvEpisodeDetails.empty(),
    name = name ?: "",
    nextEpisodeToAir = nextEpisodeToAir?.toDomain() ?: TvEpisodeDetails.empty(),
    networks = networks?.map { it.toDomain() } ?: emptyList(),
    numberOfEpisodes = numberOfEpisodes ?: 0,
    numberOfSeasons = numberOfSeasons ?: 0,
    originCountry = originCountry ?: emptyList(),
    originalLanguage = originalLanguage ?: "",
    originalName = originalName ?: "",
    overview = overview ?: "",
    popularity = popularity ?: 0f,
    posterPath = posterPath ?: "",
    productionCompanies = productionCompanies?.map { it.toDomain() } ?: emptyList(),
    seasons = seasons?.map { it.toDomain() } ?: emptyList(),
    spokenLanguages = spokenLanguages?.map { it.toDomain() } ?: emptyList(),
    status = status ?: "",
    tagline = tagline ?: "",
    type = type ?: "",
    voteAverage = voteAverage ?: 0f,
    voteCount = voteCount ?: 0,
    productionCountries = productionCountries?.map { it.toDomain() } ?: emptyList()
)

fun ContentRatingDto.toDomain(): ContentRating = ContentRating(
    descriptors = descriptors ?: emptyList(),
    countryCode = countryCode ?: "",
    rating = rating ?: ""
)


fun Genre.toEntity(mediaId: Int): GenreEntity = GenreEntity(
        uid = 0,
        id = id,
        mediaId = mediaId,
        name = name
)

fun GenreEntity.toDomain(): Genre = Genre(
    id = id,
    name = name
)

fun MovieDetails.toMovieWithGenres(mediaId: Int): MovieWithGenres = MovieWithGenres(
    movie = MovieDetailsEntity(
        uid = 0,
        adult = adult,
        backdropPath = backdropPath,
        budget = budget,
        homePage = homePage,
        id = id,
        imdbId = imdbId,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        productionCountries = productionCountries.joinToString { it.name },
        releaseDate = releaseDate,
        revenue = revenue,
        runtime = runtime,
        status = status,
        tagline = tagline,
        title = title,
        voteAverage = voteAverage,
        voteCount = voteCount
    ),
    genres = genres.map { it.toEntity(mediaId) }
)

fun MovieWithGenres.toMovieDetails(): MovieDetails = MovieDetails(
    adult = movie.adult,
    backdropPath = movie.backdropPath,
    belongsToCollection = MovieCollection.empty(),
    budget = movie.budget,
    genres = genres.map { it.toDomain() },
    homePage = movie.homePage,
    id = movie.id,
    imdbId = movie.imdbId,
    originalLanguage = movie.originalLanguage,
    originalTitle = movie.originalTitle,
    overview = movie.overview,
    popularity = movie.popularity,
    posterPath = movie.posterPath,
    productionCompanies = emptyList(),
    productionCountries = movie.productionCountries.split(", ").map { ProductionCountry("", it) },
    releaseDate = movie.releaseDate,
    revenue = movie.revenue,
    runtime = movie.runtime,
    spokenLanguages = emptyList(),
    status = movie.status,
    tagline = movie.tagline,
    title = movie.title,
    video = false,
    voteAverage = movie.voteAverage,
    voteCount = movie.voteCount
)

fun TvSeriesDetails.toTvSeriesWithGenres(mediaId: Int): TvSeriesWithGenres = TvSeriesWithGenres (
    tvSeries = TvSeriesDetailsEntity(
        uid = 0,
        adult = adult,
        backdropPath = backdropPath,
        firstAirDate = firstAirDate,
        homepage = homepage,
        id = id,
        inProduction = inProduction,
        lastAirDate = lastAirDate,
        name = name,
        numberOfEpisodes = numberOfEpisodes,
        numberOfSeasons = numberOfSeasons,
        originalLanguage = originalLanguage,
        originalName = originalName,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        productionCountries = productionCountries.joinToString { it.name },
        status = status,
        tagline = tagline,
        type = type,
        voteAverage = voteAverage,
        voteCount = voteCount
    ),
    genres = genres.map { it.toEntity(mediaId) }
)

fun TvSeriesWithGenres.toTvSeriesDetails(): TvSeriesDetails = TvSeriesDetails(
    adult = tvSeries.adult,
    backdropPath = tvSeries.backdropPath,
    createdBy = emptyList(),
    episodeRunTime = emptyList(),
    firstAirDate = tvSeries.firstAirDate,
    genres = genres.map { it.toDomain() },
    homepage = tvSeries.homepage,
    id = tvSeries.id,
    inProduction = tvSeries.inProduction,
    languages = emptyList(),
    lastAirDate = tvSeries.lastAirDate,
    lastEpisodeToAir = TvEpisodeDetails.empty(),
    name = tvSeries.name,
    nextEpisodeToAir = null,
    networks = emptyList(),
    numberOfEpisodes = tvSeries.numberOfEpisodes,
    numberOfSeasons = tvSeries.numberOfSeasons,
    originCountry = emptyList(),
    originalLanguage = tvSeries.originalLanguage,
    originalName = tvSeries.originalName,
    overview = tvSeries.overview,
    popularity = tvSeries.popularity,
    posterPath = tvSeries.posterPath,
    productionCompanies = emptyList(),
    seasons = emptyList(),
    spokenLanguages = emptyList(),
    status = tvSeries.status,
    tagline = tvSeries.tagline,
    type = tvSeries.type,
    voteAverage = tvSeries.voteAverage,
    voteCount = tvSeries.voteCount,
    productionCountries = tvSeries.productionCountries.split(", ").map { ProductionCountry("", it) }
)