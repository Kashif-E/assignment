package com.kashif.starzplayassignment.ui.navhost

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.kashif.businesslogic.domain.model.SearchDomainModel
import com.kashif.starzplayassignment.ui.details.DetailsScreen
import com.kashif.starzplayassignment.ui.details.MovieDetailsScreen
import com.kashif.starzplayassignment.ui.moviescreen.MovieScreen
import com.kashif.starzplayassignment.ui.moviescreen.MovieScreenRoute
import com.kashif.starzplayassignment.ui.playerScreen.LockScreenOrientation
import com.kashif.starzplayassignment.ui.playerScreen.PlayerScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Composable
fun MovieNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MovieScreenRoute) {
        composable<MovieScreenRoute> {
            MovieScreen(onClick = { searchDomainModel ->
                navController.navigate(MovieDetailsScreen(searchDomainModel))
            })
        }
        composable<MovieDetailsScreen>(typeMap = mapOf(typeOf<SearchDomainModel>() to CustomNavType.searchDomainModelType)) { backStackEntry ->
            val searchDomainModel = backStackEntry.toRoute<MovieDetailsScreen>()
            DetailsScreen(searchDomainModel = searchDomainModel.searchDomainModel, onBackClick = {
                navController.popBackStack()
            }, onPlayClick = {
                navController.navigate(PlayerScreen)
            })
        }
        composable<PlayerScreen> {
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            val player = ExoPlayer.Builder(LocalContext.current).build().apply {
                val mediaItem =
                    MediaItem.fromUri(Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"))
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
            PlayerScreen(player = player) {
                navController.popBackStack()
            }
        }
    }
}

object CustomNavType {
    val searchDomainModelType = object : NavType<SearchDomainModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): SearchDomainModel {
            return Json.decodeFromString(bundle.getString(key) ?: "")
        }

        override fun parseValue(value: String): SearchDomainModel {
            return Json.decodeFromString(
                Uri.decode(value)
            )
        }

        override fun put(bundle: Bundle, key: String, value: SearchDomainModel) {
            bundle.putSerializable(key, Json.encodeToString(value))
        }

        override fun serializeAsValue(value: SearchDomainModel): String {
            return Uri.encode(Json.encodeToString(value))
        }


    }
}