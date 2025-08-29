package com.koniukhov.cinecircle.core.network.api

object TMDBEndpoints {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_URL_TEMPLATE = "https://image.tmdb.org/t/p/original/%s"

    // Query parameters
    const val API_KEY = "api_key"
    const val MOVIE_ID = "movie_id"
    const val COLLECTION_ID = "collection_id"

    // Endpoints
    const val TRENDING_MOVIES = "trending/movie/day"
    const val NOW_PLAYING_MOVIES = "movie/now_playing"
    const val POPULAR_MOVIES = "movie/popular"
    const val TOP_RATED_MOVIES = "movie/top_rated"
    const val UPCOMING_MOVIES = "movie/upcoming"

    const val AIRING_TODAY_TV_SERIES = "tv/airing_today"
    const val ON_THE_AIR_TV_SERIES = "tv/on_the_air"
    const val TRENDING_TV_SERIES = "trending/tv/day"
    const val POPULAR_TV_SERIES = "tv/popular"
    const val TOP_RATED_TV_SERIES = "tv/top_rated"

    const val MOVIE_GENRES = "genre/movie/list"
    const val TV_SERIES_GENRES = "genre/tv/list"

    const val DISCOVER_MOVIES = "discover/movie"
    const val DISCOVER_TV_SERIES = "discover/tv"

    const val MOVIE_DETAILS = "movie/{$MOVIE_ID}"
    const val COLLECTION_DETAILS = "collection/{$COLLECTION_ID}"
    const val MOVIE_IMAGES = "movie/{$MOVIE_ID}/images"
    const val MOVIE_VIDEOS = "movie/{$MOVIE_ID}/videos"
    const val MOVIE_REVIEWS = "movie/{$MOVIE_ID}/reviews"
    const val MOVIE_CREDITS = "movie/{$MOVIE_ID}/credits"
    const val MOVIE_RECOMMENDATIONS = "movie/{$MOVIE_ID}/recommendations"
    const val MOVIE_SIMILAR = "movie/{$MOVIE_ID}/similar"
}