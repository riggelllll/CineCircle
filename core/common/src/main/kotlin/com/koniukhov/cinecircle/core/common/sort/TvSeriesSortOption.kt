package com.koniukhov.cinecircle.core.common.sort

import androidx.annotation.StringRes
import com.koniukhov.cinecircle.core.common.R

enum class TvSeriesSortOption(@StringRes val labelRes: Int, val apiValue: String) {
    FIRST_AIR_DATE_ASC(R.string.sort_first_air_date_asc, "first_air_date.asc"),
    FIRST_AIR_DATE_DESC(R.string.sort_first_air_date_desc, "first_air_date.desc"),

    NAME_ASC(R.string.sort_name_asc, "name.asc"),
    NAME_DESC(R.string.sort_name_desc, "name.desc"),

    ORIGINAL_NAME_ASC(R.string.sort_original_name_asc, "original_name.asc"),
    ORIGINAL_NAME_DESC(R.string.sort_original_name_desc, "original_name.desc"),

    POPULARITY_ASC(R.string.sort_popularity_asc, "popularity.asc"),
    POPULARITY_DESC(R.string.sort_popularity_desc, "popularity.desc"),

    VOTE_AVERAGE_ASC(R.string.sort_vote_average_asc, "vote_average.asc"),
    VOTE_AVERAGE_DESC(R.string.sort_vote_average_desc, "vote_average.desc"),

    VOTE_COUNT_ASC(R.string.sort_vote_count_asc, "vote_count.asc"),
    VOTE_COUNT_DESC(R.string.sort_vote_count_desc, "vote_count.desc");

    companion object {

        fun apiFromDisplayText(displayText: String, context: android.content.Context): String {
            return entries.firstOrNull { option ->
                context.getString(option.labelRes) == displayText
            }?.apiValue ?: POPULARITY_DESC.apiValue
        }
    }
}