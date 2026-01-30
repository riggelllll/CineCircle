package com.koniukhov.cinecirclex.core.network.model

import com.google.gson.annotations.SerializedName
import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID

data class TvSeriesDetailsDto(
    val adult: Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("created_by")
    val createdBy : List<CreatorDto>?,
    @SerializedName("episode_run_time")
    val episodeRunTime: List<Int>?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    val genres: List<GenreDto>?,
    val homepage: String?,
    val id: Int?,
    @SerializedName("in_production")
    val inProduction: Boolean?,
    val languages: List<String>?,
    @SerializedName("last_air_date")
    val lastAirDate: String?,
    @SerializedName("last_episode_to_air")
    val lastEpisodeToAir: TvEpisodeDetailsDto?,
    val name: String?,
    @SerializedName("next_episode_to_air")
    val nextEpisodeToAir: TvEpisodeDetailsDto?,
    val networks: List<NetworkDto>?,
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int?,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int?,
    @SerializedName("origin_country")
    val originCountry: List<String>?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_name")
    val originalName: String?,
    val overview: String?,
    val popularity: Float?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanyDto>?,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountryDto>?,
    val seasons: List<TvSeasonDetailsDto>?,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<LanguageDto>?,
    val status: String?,
    val tagline: String?,
    val type: String?,
    @SerializedName("vote_average")
    val voteAverage: Float?,
    @SerializedName("vote_count")
    val voteCount: Int?
){
    companion object{
        fun empty() = TvSeriesDetailsDto(
            adult = false,
            backdropPath = "",
            createdBy = emptyList(),
            episodeRunTime = emptyList(),
            firstAirDate = "",
            genres = emptyList(),
            homepage = "",
            id = INVALID_ID,
            inProduction = false,
            languages = emptyList(),
            lastAirDate = "",
            lastEpisodeToAir = TvEpisodeDetailsDto.empty(),
            name = "",
            nextEpisodeToAir = TvEpisodeDetailsDto.empty(),
            networks = emptyList(),
            numberOfEpisodes = 0,
            numberOfSeasons = 0,
            originCountry = emptyList(),
            originalLanguage = "",
            originalName = "",
            overview = "",
            popularity = 0f,
            posterPath = "",
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            seasons = emptyList(),
            spokenLanguages = emptyList(),
            status = "",
            tagline = "",
            type = "",
            voteAverage = 0f,
            voteCount = 0
        )
    }
}