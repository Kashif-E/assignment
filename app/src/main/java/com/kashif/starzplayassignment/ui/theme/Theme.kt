package com.kashif.starzplayassignment.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = NetflixRed,
    primaryVariant = NetflixDark,
    secondary = NetflixGrey,
    background = NetflixDark,
    surface = NetflixDark,
    onPrimary = NetflixLight,
    onSecondary = NetflixLight,
    onBackground = NetflixLight,
    onSurface = NetflixLight
)

private val LightColorPalette = lightColors(
    primary = NetflixRed,
    primaryVariant = NetflixDark,
    secondary = NetflixGrey,
    background = NetflixLight,
    surface = NetflixLight,
    onPrimary = NetflixDark,
    onSecondary = NetflixDark,
    onBackground = NetflixDark,
    onSurface = NetflixDark
)

@Composable
fun StarzPlayAssignmentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        content = content
    )
}