package com.koniukhov.cinecircle.feature.media.details.utils

import com.koniukhov.cinecircle.core.domain.model.MovieDetails
import com.koniukhov.cinecircle.core.domain.model.ReleaseDateResult
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

object MovieDetailsUtils {

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
        return String.format("%.1f", voteAverage).replace(",", ".")
    }

    fun getAgeRating(movieDetails: MovieDetails, releaseDates: List<ReleaseDateResult>, userCountryCode: String): String {
        val userCountryCertification = releaseDates
            .find { it.countryCode == userCountryCode }
            ?.releaseDates
            ?.firstOrNull { it.certification.isNotEmpty() }
            ?.certification

        if (!userCountryCertification.isNullOrBlank()) {
            return userCountryCertification
        }
        val usCertification = releaseDates
            .find { it.countryCode == "US" }
            ?.releaseDates
            ?.firstOrNull { it.certification.isNotEmpty() }
            ?.certification

        if (!usCertification.isNullOrBlank()) {
            return usCertification
        }
        return if (movieDetails.adult) "18+" else "PG-13"
    }

    fun formatReleaseYear(releaseDate: String): String {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = format.parse(releaseDate)
            val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            yearFormat.format(date!!)
        } catch (e: Exception) {
            Timber.d(e)
            releaseDate.take(4)
        }
    }

    fun getCountryCode(movieDetails: MovieDetails): String {
        return movieDetails.productionCountries.firstOrNull()?.isoName ?: "None"
    }
}
