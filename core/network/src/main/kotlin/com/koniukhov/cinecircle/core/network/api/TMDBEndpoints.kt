package com.koniukhov.cinecircle.core.network.api

object TMDBEndpoints {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_URL_TEMPLATE = "https://image.tmdb.org/t/p/original/%s"

    // Query parameters
    const val API_KEY = "api_key"
    const val MOVIE_ID = "movie_id"

    // Endpoints
    const val MOVIE_POPULAR = "movie/popular"
    const val MOVIE = "movie/{$MOVIE_ID}"
    const val CREDITS = "movie/{$MOVIE_ID}/credits"
    const val IMAGES = "movie/{$MOVIE_ID}/images"
    const val RECOMMENDATIONS = "movie/{$MOVIE_ID}/recommendations"
    const val REVIEWS = "movie/{$MOVIE_ID}/reviews"
}