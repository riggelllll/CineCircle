package com.koniukhov.cinecircle.core.common.sort

import androidx.annotation.StringRes
import com.koniukhov.cinecircle.core.common.R


enum class MovieSortOption(@StringRes val labelRes: Int, val apiValue: String) {
    ORIGINAL_TITLE_ASC(R.string.sort_original_title_asc, "original_title.asc"),
    ORIGINAL_TITLE_DESC(R.string.sort_original_title_desc, "original_title.desc"),

    POPULARITY_ASC(R.string.sort_popularity_asc, "popularity.asc"),
    POPULARITY_DESC(R.string.sort_popularity_desc, "popularity.desc"),

    REVENUE_ASC(R.string.sort_revenue_asc, "revenue.asc"),
    REVENUE_DESC(R.string.sort_revenue_desc, "revenue.desc"),

    PRIMARY_RELEASE_DATE_ASC(R.string.sort_primary_release_date_asc, "primary_release_date.asc"),
    PRIMARY_RELEASE_DATE_DESC(R.string.sort_primary_release_date_desc, "primary_release_date.desc"),

    TITLE_ASC(R.string.sort_title_asc, "title.asc"),
    TITLE_DESC(R.string.sort_title_desc, "title.desc"),

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
