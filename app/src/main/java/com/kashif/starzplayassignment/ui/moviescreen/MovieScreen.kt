package com.kashif.starzplayassignment.ui.moviescreen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kashif.businesslogic.domain.model.SearchDomainModel
import com.kashif.starzlplayassignment.R
import com.kashif.starzplayassignment.ui.moviescreen.components.MovieCard
import com.kashif.starzplayassignment.ui.moviescreen.components.ProgressIndicator
import com.kashif.starzplayassignment.ui.moviescreen.components.SearchBar
import kotlinx.serialization.Serializable
import kotlin.reflect.KFunction1

@Serializable
object MovieScreenRoute

@Composable
fun MovieScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    onClick: (SearchDomainModel) -> Unit,
) {
    val movies by viewModel.moviesState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val pagingState by viewModel.moviePagingState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(WindowInsets.systemBars), topBar = {
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.onEvent(UIEvent.UpdateSearchQuery(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester),
            )
        }, scaffoldState = scaffoldState
    ) {
        Body(movies = movies, pagingState = pagingState, scaffoldState = scaffoldState, onEvent = { event ->
            when (event) {
                is UIEvent.NavigateToDetailsScreen -> {
                    onClick(event.searchDomainModel)
                }

                else -> {
                    viewModel.onEvent(event)
                }
            }

        })
    }
}

@Composable
private fun Body(
    movies: Map<String, List<SearchDomainModel>>,
    pagingState: PagingState,
    scaffoldState: ScaffoldState,
    onEvent: (UIEvent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn {
            movies.forEach { (category, movieList) ->
                item {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(movieList, key = { item: SearchDomainModel ->
                            item.id
                        }, contentType = { item: SearchDomainModel ->
                            item::class
                        }) { movie ->
                            MovieCard(movie) {
                                onEvent(UIEvent.NavigateToDetailsScreen(movie))
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        PaginationIndicator(
            pagingState = pagingState,
            scaffoldState = scaffoldState,
            onEvent = onEvent
        )
    }
}

@Composable
fun BoxScope.PaginationIndicator(
    pagingState: PagingState,
    scaffoldState: ScaffoldState,
    retryLabel: String = stringResource(id = R.string.retry),
    onEvent: (UIEvent) -> Unit
) {
    when (pagingState) {
        PagingState.Appending -> {
            ProgressIndicator(modifier = Modifier.align(Alignment.BottomCenter))
        }

        is PagingState.Error -> {

            LaunchedEffect(key1 = pagingState) {

                val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                    pagingState.message, actionLabel = retryLabel
                )
                when (snackbarResult) {
                    SnackbarResult.Dismissed -> Log.d("snackbar", "dismissed")
                    SnackbarResult.ActionPerformed -> onEvent(UIEvent.Retry)
                }

            }

        }

        PagingState.Idle -> {
            //do nothing
        }

        PagingState.Loading -> {
            ProgressIndicator(modifier = Modifier.align(Alignment.BottomCenter))
        }

        PagingState.Success -> {
            //do nothing
        }
    }
}











