package com.levelupgamer.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta gamer
private val GamerDarkColorScheme = darkColorScheme(
    primary = BlueElectric,
    secondary = GreenLime,
    background = BlackPure,
    surface = BlackPure,
    onPrimary = WhitePure,
    onSecondary = BlackPure,
    onBackground = WhitePure,
    onSurface = WhitePure
)

@Composable
fun LevelUpGamerTheme(
    darkTheme: Boolean = true, // forzamos tema oscuro gamer
    content: @Composable () -> Unit
) {
    val colorScheme = GamerDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = GamerTypography,
        content = content
    )
}