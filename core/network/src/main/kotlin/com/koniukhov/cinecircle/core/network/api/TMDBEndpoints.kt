package com.koniukhov.cinecircle.core.network.api

object TMDBEndpoints {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_URL_TEMPLATE = "https://image.tmdb.org/t/p/original/%s"

    // Query parameters
    const val API_KEY = "api_key"
    const val MOVIE_ID = "movie_id"

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


    const val MOVIE = "movie/{$MOVIE_ID}"
    const val CREDITS = "movie/{$MOVIE_ID}/credits"
    const val IMAGES = "movie/{$MOVIE_ID}/images"
    const val RECOMMENDATIONS = "movie/{$MOVIE_ID}/recommendations"
    const val REVIEWS = "movie/{$MOVIE_ID}/reviews"
}