package com.koniukhov.cinecirclex.core.common.navigation

import android.net.Uri
import androidx.core.net.toUri
import com.koniukhov.cinecirclex.core.common.model.MediaListType

object NavArgs {
    const val ARG_TYPE = "type"
    const val ARG_TITLE = "title"
    const val ARG_GENRE_ID = "genre_id"
    const val ARG_MOVIE_ID = "movie_id"
    const val ARG_TV_SERIES_ID = "tv_series_id"

    fun mediaListUri(type: MediaListType, encodedTitle: String, genreId: Int): Uri =
        "app://cinecircle/mediaList?$ARG_TYPE=$type&$ARG_TITLE=${encodedTitle}&${ARG_GENRE_ID}=${genreId}".toUri()

    fun movieDetailsUri(movieId: Int): Uri =
        "app://cinecircle/movie_details?$ARG_MOVIE_ID=$movieId".toUri()

    fun tvSeriesDetailsUri(tvSeriesId: Int): Uri =
        "app://cinecircle/tv_series_details?$ARG_TV_SERIES_ID=$tvSeriesId".toUri()
}