package com.kashif.businesslogic.data.remote.service

import com.kashif.businesslogic.data.remote.dto.MovieDetailsDTO
import com.kashif.businesslogic.data.remote.dto.SearchResultDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbService {
    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String
    ): Response<SearchResultDto>

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): Response<MovieDetailsDTO>

}