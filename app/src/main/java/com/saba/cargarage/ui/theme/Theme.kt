package com.saba.cargarage.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Navy800,
    onPrimary = Cloud,
    secondary = Amber,
    onSecondary = Navy900,
    tertiary = AmberDark,
    background = Cloud,
    onBackground = Navy900,
    surface = Color.White,
    onSurface = Navy900,
    surfaceVariant = Color(0xFFE7EBF3),
    onSurfaceVariant = SlateGrey,
    error = DangerRed
)

private val DarkColors = darkColorScheme(
    primary = Amber,
    onPrimary = Navy900,
    secondary = Amber,
    onSecondary = Navy900,
    background = Navy900,
    onBackground = Cloud,
    surface = Navy800,
    onSurface = Cloud,
    surfaceVariant = Navy700,
    onSurfaceVariant = Color(0xFFB6C0D9),
    error = DangerRed
)

// Rounder-than-Material shapes to reinforce the custom look.
private val AppShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(28.dp)
)

@Composable
fun CarGarageTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
