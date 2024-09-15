package com.kashif.starzplayassignment.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kashif.businesslogic.domain.model.SearchDomainModel
import com.kashif.starzlplayassignment.R
import kotlinx.serialization.Serializable

/**
 * type safe route for details screen
 */
@Serializable
class MovieDetailsScreen(val searchDomainModel: SearchDomainModel)

@Composable
fun DetailsScreen(searchDomainModel: SearchDomainModel, onPlayClick: () -> Unit, onBackClick: () -> Unit) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.surface,
                title = { Text(text = "Details") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            MovieDetailsContent(
                movie = searchDomainModel,
                modifier = Modifier.padding(padding),
                onPlayClick = onPlayClick
            )
        }
    )
}

@Composable
fun MovieDetailsContent(modifier: Modifier = Modifier, movie: SearchDomainModel, onPlayClick: () -> Unit) {
    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = movie.backdropPath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(200.dp))

            Text(
                text = movie.title,
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Text(
                text = movie.overview,
                style = MaterialTheme.typography.body2.copy(
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Release Date: ${movie.releaseDate}",
                style = MaterialTheme.typography.body2.copy(
                    color = Color.White
                )
            )

            Text(
                text = "Rating: ${movie.rating}",
                style = MaterialTheme.typography.body2.copy(
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(16.dp))

            when (movie.mediaType) {
                "movie", "tv" -> {
                    Button(
                        onClick = { onPlayClick() },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Play", style = MaterialTheme.typography.h6)
                    }
                }

                "person" -> {
                    Column(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Biography: ${movie.knownFor.first().overview}",
                            style = MaterialTheme.typography.body1.copy(
                                color = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Known for: ${movie.knownFor.joinToString(", ")}",
                            style = MaterialTheme.typography.body1.copy(
                                color = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
