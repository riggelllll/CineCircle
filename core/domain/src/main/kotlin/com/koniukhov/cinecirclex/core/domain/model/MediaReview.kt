package com.koniukhov.cinecirclex.core.domain.model

import java.io.Serializable

data class MediaReview(
    val author: String,
    val authorDetails: ReviewAuthor,
    val content: String,
    val createdAt: String,
    val id: String,
    val updatedAt: String,
    val url: String
) : Serializable