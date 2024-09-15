package com.kashif.starzplayassignment.ui.moviescreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kashif.businesslogic.domain.model.SearchDomainModel

@Composable
fun MovieCard(movie: SearchDomainModel, onClick: () -> Unit) {
    val rememberedClick = remember {
        Modifier.clickable { onClick() }
    }
    Card(
        modifier = Modifier
            .width(120.dp)
            .aspectRatio(2f / 3f).then(rememberedClick),
        elevation = 4.dp,
        shape = RoundedCornerShape(14.dp)
    ) {
        AsyncImage(
            model = movie.posterPath,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}