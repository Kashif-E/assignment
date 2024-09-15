package com.kashif.businesslogic.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class KnownFor(
    val adult: Boolean = false,
    @SerialName("backdrop_path") val backdropPath: String? = "",
    val id: Int = 0,
    @SerialName("media_type") val mediaType: String = "",
    @SerialName("original_language") val originalLanguage: String = "",
    @SerialName("original_title") val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    @SerialName("poster_path") val posterPath: String = "",
    @SerialName("release_date") val releaseDate: String = "",
    val title: String = "",
    val video: Boolean = false,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0
)