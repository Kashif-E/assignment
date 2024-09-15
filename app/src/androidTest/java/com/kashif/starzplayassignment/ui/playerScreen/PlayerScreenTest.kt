package com.kashif.starzplayassignment.ui.playerScreen

import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.exoplayer2.ExoPlayer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

@Composable
fun TestNavHost(mockPlayer: ExoPlayer) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "player") {
        composable("player") {
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            PlayerScreen(player = mockPlayer) {
                navController.popBackStack()
            }
        }
    }
}

class PlayerScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    @Test
    fun testPlayerScreen() {
        val mockPlayer = mockk<ExoPlayer>(relaxed = true)
        every { mockPlayer.setMediaItem(any()) } returns Unit
        every { mockPlayer.prepare() } returns Unit
        every { mockPlayer.playWhenReady = true } returns Unit

        // Set custom TestNavHost as the content to test
        composeTestRule.setContent {
            TestNavHost(mockPlayer)
        }

        // Verify that the ExoPlayer methods are called as expected
        verify { mockPlayer.setMediaItem(any()) }
        verify { mockPlayer.prepare() }
        verify { mockPlayer.playWhenReady = true }
    }
}