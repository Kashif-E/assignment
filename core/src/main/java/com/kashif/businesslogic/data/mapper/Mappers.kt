package com.kashif.businesslogic.data.mapper


import com.kashif.businesslogic.data.remote.dto.SearchResultDto
import com.kashif.businesslogic.domain.model.SearchDomainModel
import com.kashif.businesslogic.domain.model.KnownForDomainModel
import com.kashif.businesslogic.domain.model.MoviesDomainModel
import com.kashif.businesslogic.domain.util.Constants
import com.kashif.businesslogic.domain.util.formatDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 *
 * Search Result DTO to Domain Model Mapper
 */
suspend fun SearchResultDto.asDomainModel(): List<SearchDomainModel> {
    return withContext(Dispatchers.Default) {
        this@asDomainModel.results.map { result ->
            SearchDomainModel(
                id = result.id.toString(),
                title = result.title ?: result.name,
                overview = result.overview,
                releaseDate = (result.releaseDate?.let { formatDate(it) } ?: "Unknown"),
                rating = result.voteAverage.toString(),
                posterPath = Constants.TMDB_IMAGE_URL + result.posterPath,
                backdropPath = Constants.TMBD_BACKDROP_URL + result.backdropPath,
                knownFor = result.knownFor?.map { knownFor ->
                    KnownForDomainModel(
                        id = knownFor.id.toString(),
                        title = knownFor.title,
                        overview = knownFor.overview,
                        releaseDate = formatDate(knownFor.releaseDate),
                        rating = String.format(Locale.UK, "%.1f", knownFor.voteAverage),
                        posterPath = Constants.TMDB_IMAGE_URL + knownFor.posterPath,
                        backdropPath = Constants.TMBD_BACKDROP_URL + knownFor.backdropPath.toString(),
                        mediaType = knownFor.mediaType,
                        popularity = knownFor.popularity.toString(),
                        voteCount = knownFor.voteCount.toString()
                    )
                } ?: emptyList(),
                mediaType = result.mediaType,
                popularity = result.popularity.toString(),
                voteCount = result.voteCount.toString()
            )
        }
    }
}

