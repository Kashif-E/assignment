package com.kashif.businesslogic.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val adult: Boolean = false,
    @SerialName("backdrop_path") val backdropPath: String? = "",
    @SerialName("first_air_date") val firstAirDate: String = "",
    val id: Int = 0,
    @SerialName("known_for") val knownFor: List<KnownFor>? = emptyList(),
    @SerialName("known_for_department") val knownForDepartment: String? = "",
    @SerialName("media_type") val mediaType: String = "",
    val name: String = "",
    @SerialName("origin_country") val originCountry: List<String> = emptyList(),
    @SerialName("original_language") val originalLanguage: String = "",
    @SerialName("original_name") val originalName: String = "",
    @SerialName("original_title") val originalTitle: String? = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    @SerialName("poster_path") val posterPath: String = "",
    @SerialName("profile_path") val profilePath: String? = "",
    @SerialName("release_date") val releaseDate: String? = "",
    val title: String? = "",
    val video: Boolean? = false,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0
)