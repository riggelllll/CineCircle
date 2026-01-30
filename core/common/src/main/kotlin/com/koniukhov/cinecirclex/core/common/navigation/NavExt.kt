package com.koniukhov.cinecirclex.core.common.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.koniukhov.cinecirclex.core.common.navigation.NavArgs.movieDetailsUri
import com.koniukhov.cinecirclex.core.common.navigation.NavArgs.tvSeriesDetailsUri

fun NavController.navigateToTvSeriesDetails(tvSeriesId: Int) {
    val request = NavDeepLinkRequest.Builder
        .fromUri(tvSeriesDetailsUri(tvSeriesId))
        .build()
    this.navigate(request)
}

fun NavController.navigateToMovieDetails(movieId: Int) {
    val request = NavDeepLinkRequest.Builder
        .fromUri(movieDetailsUri(movieId))
        .build()
    this.navigate(request)
}