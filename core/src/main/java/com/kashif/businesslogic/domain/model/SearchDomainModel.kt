package com.kashif.businesslogic.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchDomainModel(
    val id: String,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val rating: String,
    val posterPath: String,
    val backdropPath: String,
    val knownFor: List<KnownForDomainModel>,
    val mediaType: String,
    val popularity: String,
    val voteCount: String
)
@Serializable
data class KnownForDomainModel(
    val id: String,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val rating: String,
    val posterPath: String,
    val backdropPath: String,
    val mediaType: String,
    val popularity: String,
    val voteCount: String
)