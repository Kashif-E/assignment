package com.kashif.businesslogic.data.repository

import androidx.annotation.WorkerThread
import com.kashif.businesslogic.data.remote.dto.MovieDetailsDTO
import com.kashif.businesslogic.data.remote.dto.SearchResultDto
import com.kashif.businesslogic.data.remote.service.MovieDbService
import retrofit2.Response

class MovieRepositoryImpl(private val movieDbService: MovieDbService) : IMovieRepository {
    @WorkerThread
    override suspend fun searchMovies(query: String): Response<SearchResultDto> {
        return movieDbService.searchMulti(query)
    }

    override suspend fun getMovieDetails(id: Int): Response<MovieDetailsDTO> {
        return movieDbService.getMovieDetails(id)
    }
}