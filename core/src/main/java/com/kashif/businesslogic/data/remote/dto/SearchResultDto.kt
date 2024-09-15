package com.kashif.businesslogic.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultDto(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)