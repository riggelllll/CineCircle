package com.koniukhov.cinecirclex.core.domain.model

import java.io.Serializable

data class ReviewAuthor(
    val name: String,
    val username: String,
    val avatarPath: String,
    val rating: String
) : Serializable {
    companion object{
        fun empty() = ReviewAuthor(
            name = "",
            username = "",
            avatarPath = "",
            rating = ""
        )
    }
}
