package com.koniukhov.cinecircle.core.domain.model

import com.koniukhov.cinecircle.core.common.Constants.VIDEO_SITE_YOUTUBE
import com.koniukhov.cinecircle.core.common.Constants.VIDEO_TYPE_TRAILER

data class MovieVideos(
    val id: Int,
    val results: List<Video>
){
    fun getYouTubeTrailers(): List<Video> {
        return results.filter {
            it.site.equals(VIDEO_SITE_YOUTUBE, ignoreCase = true) &&
            it.type.equals(VIDEO_TYPE_TRAILER, ignoreCase = true)
        }
    }
}