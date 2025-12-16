package com.example.blood_donation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = RedPrimary,
    secondary = RedSecondary,
    tertiary = RedTertiary,
    background = DarkBackground,
    surface = CardBackground,
    onPrimary = Color.White,       // text on buttons
    onSecondary = Color.White,     // text on secondary buttons
    onTertiary = Color.White,      // text on tertiary surfaces
    onBackground = GreyLight,      // default text color
    onSurface = GreyLight,         // text on cards
    error = ErrorMaroon,
    onError = Color.White
)

private val LightColorPalette = lightColorScheme(
    primary = RedPrimary,
    secondary = RedSecondary,
    tertiary = RedTertiary,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = ErrorMaroon,
    onError = Color.White
)

@Composable
fun BloodDonationTheme(
    darkTheme: Boolean = true,   // force dark mode
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,  // your Typography.kt
        shapes = Shapes,          // your Shapes.kt
        content = content
    )
}
