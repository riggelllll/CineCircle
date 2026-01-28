package com.koniukhov.cinecircle.core.network.api

object TMDBEndpoints {
    const val BASE_URL = "https://api.themoviedb.org/3/"

    object ImageSizes {
        private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p"

        const val POSTER_SMALL = "$BASE_IMAGE_URL/w185/%s"
        const val POSTER_MEDIUM = "$BASE_IMAGE_URL/w342/%s"
        const val POSTER_LARGE = "$BASE_IMAGE_URL/w500/%s"
        const val POSTER_XLARGE = "$BASE_IMAGE_URL/w780/%s"
        const val POSTER_ORIGINAL = "$BASE_IMAGE_URL/original/%s"

        const val BACKDROP_SMALL = "$BASE_IMAGE_URL/w300/%s"
        const val BACKDROP_MEDIUM = "$BASE_IMAGE_URL/w780/%s"
        const val BACKDROP_LARGE = "$BASE_IMAGE_URL/w1280/%s"
        const val BACKDROP_ORIGINAL = "$BASE_IMAGE_URL/original/%s"

        const val PROFILE_SMALL = "$BASE_IMAGE_URL/w45/%s"
        const val PROFILE_MEDIUM = "$BASE_IMAGE_URL/w185/%s"
        const val PROFILE_LARGE = "$BASE_IMAGE_URL/h632/%s"
        const val PROFILE_ORIGINAL = "$BASE_IMAGE_URL/original/%s"

        const val STILL_SMALL = "$BASE_IMAGE_URL/w185/%s"
        const val STILL_MEDIUM = "$BASE_IMAGE_URL/w300/%s"
        const val STILL_LARGE = "$BASE_IMAGE_URL/w500/%s"
        const val STILL_ORIGINAL = "$BASE_IMAGE_URL/original/%s"
    }

    object QueryParams {
        const val API_KEY = "api_key"
        const val MOVIE_ID = "movie_id"
        const val TV_SERIES_ID = "series_id"
        const val TV_SEASON_NUMBER = "season_number"
        const val TV_EPISODE_NUMBER = "episode_number"
        const val COLLECTION_ID = "collection_id"
        const val MOVIE = "movie"
        const val TV = "tv"
    }

    object Movies {
        const val TRENDING = "trending/${QueryParams.MOVIE}/day"
        const val NOW_PLAYING = "${QueryParams.MOVIE}/now_playing"
        const val POPULAR = "${QueryParams.MOVIE}/popular"
        const val TOP_RATED = "${QueryParams.MOVIE}/top_rated"
        const val UPCOMING = "${QueryParams.MOVIE}/upcoming"
        const val GENRES = "genre/${QueryParams.MOVIE}/list"
        const val DISCOVER = "discover/${QueryParams.MOVIE}"
        const val DETAILS = "${QueryParams.MOVIE}/{${QueryParams.MOVIE_ID}}"
        const val COLLECTION_DETAILS = "collection/{${QueryParams.COLLECTION_ID}}"
        const val IMAGES = "${QueryParams.MOVIE}/{${QueryParams.MOVIE_ID}}/images"
        const val VIDEOS = "${QueryParams.MOVIE}/{${QueryParams.MOVIE_ID}}/videos"
        const val REVIEWS = "${QueryParams.MOVIE}/{${QueryParams.MOVIE_ID}}/reviews"
        const val CREDITS = "${QueryParams.MOVIE}/{${QueryParams.MOVIE_ID}}/credits"
        const val RECOMMENDATIONS = "${QueryParams.MOVIE}/{${QueryParams.MOVIE_ID}}/recommendations"
        const val SIMILAR = "${QueryParams.MOVIE}/{${QueryParams.MOVIE_ID}}/similar"
        const val RELEASE_DATES = "${QueryParams.MOVIE}/{${QueryParams.MOVIE_ID}}/release_dates"
        const val SEARCH = "search/${QueryParams.MOVIE}"
    }

    object TVSeries {
        const val TRENDING = "trending/${QueryParams.TV}/day"
        const val AIRING_TODAY = "${QueryParams.TV}/airing_today"
        const val ON_THE_AIR = "${QueryParams.TV}/on_the_air"
        const val POPULAR = "${QueryParams.TV}/popular"
        const val TOP_RATED = "${QueryParams.TV}/top_rated"
        const val GENRES = "genre/${QueryParams.TV}/list"
        const val DISCOVER = "discover/${QueryParams.TV}"
        const val DETAILS = "${QueryParams.TV}/{${QueryParams.TV_SERIES_ID}}"
        const val SEASON_DETAILS = "${QueryParams.TV}/{${QueryParams.TV_SERIES_ID}}/season/{${QueryParams.TV_SEASON_NUMBER}}"
        const val EPISODE_DETAILS = "${QueryParams.TV}/{${QueryParams.TV_SERIES_ID}}/season/{${QueryParams.TV_SEASON_NUMBER}}/episode/{${QueryParams.TV_EPISODE_NUMBER}}"
        const val IMAGES = "${QueryParams.TV}/{${QueryParams.TV_SERIES_ID}}/images"
        const val VIDEOS = "${QueryParams.TV}/{${QueryParams.TV_SERIES_ID}}/videos"
        const val REVIEWS = "${QueryParams.TV}/{${QueryParams.TV_SERIES_ID}}/reviews"
        const val CREDITS = "${QueryParams.TV}/{${QueryParams.TV_SERIES_ID}}/credits"
        const val CONTENT_RATINGS = "${QueryParams.TV}/{${QueryParams.TV_SERIES_ID}}/content_ratings"
        const val RECOMMENDATIONS = "${QueryParams.TV}/{${QueryParams.TV_SERIES_ID}}/recommendations"
        const val SIMILAR = "${QueryParams.TV}/{${QueryParams.TV_SERIES_ID}}/similar"
        const val SEARCH = "search/${QueryParams.TV}"
    }
}