package com.koniukhov.cinecirclex.core.network.model

import com.google.gson.annotations.SerializedName
import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID

data class MovieDetailsDto (
    val adult: Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: MovieCollectionDto?,
    val budget: Int?,
    val genres: List<GenreDto>?,
    @SerializedName("homepage")
    val homePage: String?,
    val id: Int?,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    val overview: String?,
    val popularity: Float?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanyDto>?,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountryDto>?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val revenue: Long?,
    val runtime: Int?,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<LanguageDto>?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean?,
    @SerializedName("vote_average")
    val voteAverage: Float?,
    @SerializedName("vote_count")
    val voteCount: Int?
){
    companion object {
        fun empty() = MovieDetailsDto(
            adult = false,
            backdropPath = "",
            belongsToCollection = MovieCollectionDto.empty(),
            budget = 0,
            genres = emptyList(),
            homePage = "",
            id = INVALID_ID,
            imdbId = "",
            originalLanguage = "",
            originalTitle = "",
            overview = "",
            popularity = 0f,
            posterPath = "",
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            releaseDate = "",
            revenue = 0,
            runtime = 0,
            spokenLanguages = emptyList(),
            status = "",
            tagline = "",
            title = "",
            video = false,
            voteAverage = 0f,
            voteCount = 0
        )
    }
}