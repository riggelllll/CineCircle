package com.koniukhov.cinecircle.core.domain.model

data class ReviewAuthor(
    val name: String,
    val username: String,
    val avatarPath: String,
    val rating: String
){
    companion object{
        fun empty() = ReviewAuthor(
            name = "",
            username = "",
            avatarPath = "",
            rating = ""
        )
    }
}
