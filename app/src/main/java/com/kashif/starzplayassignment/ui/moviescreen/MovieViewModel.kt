package com.kashif.starzplayassignment.ui.moviescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kashif.businesslogic.di.defaultDispatcher
import com.kashif.businesslogic.di.ioDispatcher
import com.kashif.businesslogic.domain.model.SearchDomainModel
import com.kashif.businesslogic.domain.usecase.MultiSearchMoviesUseCase
import com.kashif.businesslogic.domain.util.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val useCase: MultiSearchMoviesUseCase,
    @ioDispatcher private val ioDispatcher: CoroutineDispatcher,
    @defaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var currentPage = 1
    private val _moviePagingState = MutableStateFlow<PagingState>(PagingState.Idle)
    val moviePagingState = _moviePagingState.asStateFlow()
    private val _movies = MutableStateFlow<Map<String, List<SearchDomainModel>>>(hashMapOf())
    val moviesState = _movies.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchMoviesState by lazy {
        searchQuery.debounce(300L).filter { query ->
            query.isNotEmpty()
        }.map { query ->
            val queryCharacters = query.toCharArray().filter { it.isWhitespace().not() }
            moviesState.value.values.flatten().filter { data ->
                queryCharacters.all { char ->
                    data.title.contains(char, true)
                }
            }.sortedBy { queryCharacters[0] }
        }.stateIn(viewModelScope + defaultDispatcher, SharingStarted.Eagerly, emptyList())
    }

    fun getCurrentPage() = currentPage

    fun onEvent(event: UIEvent) {
        when (event) {
            UIEvent.FetchNextPage -> fetchNextPage()
            is UIEvent.UpdateSearchQuery -> updateSearch(event.query)
            UIEvent.Retry -> fetchNextPage()
            else->{
                // do nothing
            }
        }
    }

    init {
        onEvent(UIEvent.FetchNextPage)
    }

    private fun fetchNextPage() {
        if (_moviePagingState.value !is PagingState.Loading && _moviePagingState.value !is PagingState.Appending) {
            getMovies()
        }
    }

    private fun getMovies() {
        viewModelScope.launch(ioDispatcher) {
            useCase(searchQuery.value).asResult().collectLatest { result ->
                when (result) {
                    is com.kashif.businesslogic.domain.util.Result.Error -> {
                        _moviePagingState.update { PagingState.Error(result.errorMessage) }
                    }

                    com.kashif.businesslogic.domain.util.Result.Loading -> {
                        _moviePagingState.update {
                            if (currentPage > 1) {
                                PagingState.Appending
                            } else {
                                PagingState.Loading
                            }
                        }
                    }

                    is com.kashif.businesslogic.domain.util.Result.Success -> {
                        currentPage++
                        _moviePagingState.update { PagingState.Success }
                        _movies.update { movies ->
                            if (currentPage == 1) result.data else movies + result.data
                        }
                    }
                }
            }
        }
    }

    private fun updateSearch(searchText: String) {
        _searchQuery.update { searchText }
        currentPage = 1
        _movies.value = hashMapOf()
        fetchNextPage()
    }
}

sealed interface PagingState {
    data object Idle : PagingState
    data object Loading : PagingState
    data object Success : PagingState
    data object Appending : PagingState
    data class Error(val message: String) : PagingState
}


sealed interface UIEvent {
    data object FetchNextPage : UIEvent
    data object Retry : UIEvent
    data class UpdateSearchQuery(val query: String) : UIEvent
    data class NavigateToDetailsScreen(val searchDomainModel: SearchDomainModel) :
        UIEvent
}