package com.koniukhov.cinecircle.feature.media.details.utils

import com.koniukhov.cinecircle.core.common.Constants.DEFAULT_COUNTRY_CODE
import com.koniukhov.cinecircle.core.domain.model.ContentRating
import com.koniukhov.cinecircle.core.domain.model.MovieDetails
import com.koniukhov.cinecircle.core.domain.model.ReleaseDateResult
import com.koniukhov.cinecircle.core.domain.model.TvSeriesDetails
import java.util.Locale
import kotlin.collections.find
import kotlin.collections.firstOrNull

object MediaDetailsUtils {

    fun formatRuntime(
        runtime: Int,
        hoursLabel: String,
        minutesLabel: String
    ): String {
        val hours = runtime / 60
        val minutes = runtime % 60
        return if (hours > 0) {
            "${hours}$hoursLabel ${minutes}$minutesLabel"
        } else {
            "${minutes}$minutesLabel"
        }
    }

    fun formatRating(voteAverage: Float): String {
        return String.format(Locale.US,"%.1f", voteAverage)
    }

    fun getAgeRating(movieDetails: MovieDetails, releaseDates: List<ReleaseDateResult>, userCountryCode: String): String {
        val userCountryCertification = releaseDates
            .find { it.countryCode == userCountryCode }
            ?.releaseDates
            ?.firstOrNull { it.certification?.isNotEmpty() == true }
            ?.certification

        if (!userCountryCertification.isNullOrBlank()) {
            return userCountryCertification
        }
        val usCertification = releaseDates
            .find { it.countryCode == DEFAULT_COUNTRY_CODE }
            ?.releaseDates
            ?.firstOrNull { it.certification?.isNotEmpty() == true }
            ?.certification

        if (!usCertification.isNullOrBlank()) {
            return usCertification
        }
        return if (movieDetails.adult) "18+" else "PG-13"
    }

    fun getTvSeriesAgeRating(contentRatings: List<ContentRating>, userCountryCode: String): String {
        val userCountryCertification = contentRatings
            .find { it.countryCode == userCountryCode }
            ?.rating

        if (!userCountryCertification.isNullOrBlank()) {
            return userCountryCertification
        }

        val usCertification = contentRatings
            .find { it.countryCode == DEFAULT_COUNTRY_CODE }
            ?.rating

        if (!usCertification.isNullOrBlank()) {
            return usCertification
        }

        return contentRatings.firstOrNull()?.rating ?: "NR"
    }


    fun getMovieCountryCode(movieDetails: MovieDetails): String {
        return movieDetails.productionCountries.firstOrNull()?.countryCode ?: "None"
    }

    fun getTvSeriesCountryCode(tvSeriesDetails: TvSeriesDetails): String {
        return tvSeriesDetails.productionCountries.firstOrNull()?.countryCode ?: "None"
    }
}
