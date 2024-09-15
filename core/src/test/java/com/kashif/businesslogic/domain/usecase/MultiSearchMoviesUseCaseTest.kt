package com.kashif.businesslogic.domain.usecase

import com.kashif.businesslogic.data.mapper.asDomainModel
import com.kashif.businesslogic.data.remote.dto.Result
import com.kashif.businesslogic.data.remote.dto.SearchResultDto
import com.kashif.businesslogic.data.repository.IMovieRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class MultiSearchMoviesUseCaseTest {

    private lateinit var multiSearchMoviesUseCase: MultiSearchMoviesUseCase
    private val movieRepository: IMovieRepository = mockk()

    @BeforeEach
    fun setUp() {
        multiSearchMoviesUseCase = MultiSearchMoviesUseCase(movieRepository)
    }

    @Test
    fun `invoke should return grouped movies when response is successful`() = runBlocking {
        // Given
        val query = "test"
        val searchResultDto = SearchResultDto(
            page = 1,
            results = listOf(
                Result(
                    id = 1,
                    title = "Movie 1",
                    overview = "Overview 1",
                    mediaType = "movie",
                    voteAverage = 8.0,
                    posterPath = "poster1.jpg",
                    releaseDate = "2021-01-01"
                ),
                Result(
                    id = 2,
                    title = "Movie 2",
                    overview = "Overview 2",
                    mediaType = "movie",
                    voteAverage = 9.0,
                    posterPath = "poster2.jpg",
                    releaseDate = "2022-01-01"
                )
            ),
            total_pages = 1,
            total_results = 2
        )

        val movies = searchResultDto.asDomainModel()
        val response = Response.success(searchResultDto)

        // Mock the repository to return the successful response
        coEvery { movieRepository.searchMovies(query) } returns response

        // When
        val result = multiSearchMoviesUseCase(query).first()

        // Then
        val expected = movies.groupBy { it.mediaType }.toSortedMap()
        assertEquals(expected, result)
    }

    @Test
    fun `invoke should throw exception when response is not successful`() = runBlocking {
        // Given
        val query = "test"
        val errorMessage = "Error"
        val responseBody = errorMessage.toResponseBody("text/plain".toMediaTypeOrNull())
        val response = Response.error<SearchResultDto>(400, responseBody)

        // Mock the repository to return the error response
        coEvery { movieRepository.searchMovies(query) } returns response

        // When / Then
        try {
            multiSearchMoviesUseCase(query).collect()
        } catch (e: Exception) {
            assertEquals("HTTP 400 Response.error()", e.message)
        }
    }
}