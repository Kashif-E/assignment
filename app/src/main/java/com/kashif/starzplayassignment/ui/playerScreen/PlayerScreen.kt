package com.kashif.starzplayassignment.ui.playerScreen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.kashif.starzlplayassignment.R
import kotlinx.serialization.Serializable


@Serializable
object PlayerScreen

@Composable
fun PlayerScreen( player: ExoPlayer,onBackClick: () -> Unit,) {


    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Player") },
                navigationIcon = {
                    IconButton(onClick = {
                        player.release()
                        onBackClick()
                    }) {
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(factory = {
                    PlayerView(it).apply {
                        this.player = player
                    }
                })
            }
        }
    )

    BackHandler {
        player.release()
    }


}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val activity = LocalContext.current as Activity
    DisposableEffect(Unit) {
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}