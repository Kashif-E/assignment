package com.kashif.starzplayassignment.ui.details


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kashif.businesslogic.di.defaultDispatcher
import com.kashif.businesslogic.di.ioDispatcher
import com.kashif.businesslogic.domain.model.MoviesDomainModel
import com.kashif.businesslogic.domain.util.Result
import com.kashif.businesslogic.domain.util.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    @ioDispatcher private val ioDispatcher: CoroutineDispatcher,
    @defaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _movieDetails: MutableStateFlow<MovieDetailsState> =
        MutableStateFlow(MovieDetailsState.Idle)
    val movieDetails = _movieDetails.asStateFlow()
    fun onEvent(event: DetailsScreenEvent) {
        when (event) {
            is DetailsScreenEvent.GetMovieDetails -> {
                getMovieDetails(event.movieId)
            }
        }
    }

    private fun getMovieDetails(movieId: Int) {
        _movieDetails.update { MovieDetailsState.Loading }
        viewModelScope.launch(ioDispatcher) {
            getMovieDetailsUseCase(movieId).asResult().collect { result ->
                when (result) {
                    is Result.Error -> {
                        _movieDetails.update {
                            MovieDetailsState.Error(result.errorMessage)
                        }
                    }

                    Result.Loading -> {
                        _movieDetails.update {
                            MovieDetailsState.Loading
                        }
                    }

                    is Result.Success -> {
                        result.data?.let { model ->
                            _movieDetails.update {
                                MovieDetailsState.Success(model)
                            }

                        } ?: run {
                            _movieDetails.update {
                                MovieDetailsState.Error("No data found")
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class DetailsScreenEvent {
    data class GetMovieDetails(val movieId: Int) : DetailsScreenEvent()
}

sealed class MovieDetailsState {
    data object Idle : MovieDetailsState()
    data object Loading : MovieDetailsState()
    data class Success(val movie: MoviesDomainModel) : MovieDetailsState()
    data class Error(val message: String) : MovieDetailsState()
}