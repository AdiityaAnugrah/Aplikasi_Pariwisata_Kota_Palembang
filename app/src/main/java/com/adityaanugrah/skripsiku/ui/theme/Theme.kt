package com.adityaanugrah.skripsiku.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

private val DarkColorScheme = darkColors(
//    primary =  md_theme_dark_primary,
//    onPrimary =  md_theme_dark_onPrimary,
    onError = red,
    onSurface = grey,
    primary = Purple80,
    secondary = PurpleGrey80,
//
    background = white,


)

private val LightColorScheme = lightColors(
//    primary = md_theme_light_primary,
//    onPrimary = md_theme_light_onPrimary,
    onError = red,
    primary = Purple40,
    secondary = PurpleGrey40,
    onSurface = grey,
//
    background = white,


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun SkripsikuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colors = colorScheme,
        typography = Typography,
        content = content
    )
}

@Preview
@Composable
fun DarkThemePreview() {
    SkripsikuTheme(darkTheme = true) {
    }
}