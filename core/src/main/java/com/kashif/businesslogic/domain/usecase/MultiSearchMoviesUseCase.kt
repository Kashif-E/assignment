package com.kashif.businesslogic.domain.usecase

import com.kashif.businesslogic.data.mapper.asDomainModel
import com.kashif.businesslogic.data.repository.IMovieRepository
import kotlinx.coroutines.flow.flow

class MultiSearchMoviesUseCase(private val movieRepository: IMovieRepository) {
    operator fun invoke(query: String) = flow {
        val response = movieRepository.searchMovies(query)
        if (response.isSuccessful) {
            response.body()?.asDomainModel()?.let { movies ->
                val groupedMovies = movies.groupBy { it.mediaType }
                    .toSortedMap()
                emit(groupedMovies)
            } ?: emit(emptyMap())
        } else {
            throw Exception(response.message())
        }
    }
}