package com.koniukhov.cinecircle.core.domain.model

import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID
import com.koniukhov.cinecircle.core.common.Constants.VIDEO_SITE_YOUTUBE
import com.koniukhov.cinecircle.core.common.Constants.VIDEO_TYPE_TEASER
import com.koniukhov.cinecircle.core.common.Constants.VIDEO_TYPE_TRAILER

data class MediaVideos(
    val id: Int,
    val results: List<Video>
){
    fun getYouTubeTrailersAndTeasers(): List<Video> {
        return results.filter {
            it.site.equals(VIDEO_SITE_YOUTUBE, ignoreCase = true) &&
            (it.type.equals(VIDEO_TYPE_TRAILER, ignoreCase = true) ||
            it.type.equals(VIDEO_TYPE_TEASER, ignoreCase = true))
        }.sortedBy { it.type != VIDEO_TYPE_TRAILER }
    }

    companion object {
        fun empty() = MediaVideos(INVALID_ID, emptyList())
    }
}