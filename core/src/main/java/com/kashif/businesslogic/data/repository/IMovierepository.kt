package com.kashif.businesslogic.data.repository


import com.kashif.businesslogic.data.remote.dto.SearchResultDto
import retrofit2.Response

interface IMovieRepository {
    suspend fun searchMovies(query: String): Response<SearchResultDto>

}