package com.ai.assistant.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NavyDark, onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF17466F), onPrimaryContainer = Color(0xFFD6EAFF),
    tertiary = Color(0xFF64D6AD), background = DarkBackground, onBackground = DarkInk,
    surface = DarkSurface, onSurface = DarkInk, surfaceContainer = DarkSurface,
    surfaceContainerHigh = DarkSurfaceHigh, onSurfaceVariant = Color(0xFFB7C5D2),
)

private val LightColorScheme = lightColorScheme(
    primary = Navy, onPrimary = Color.White,
    primaryContainer = BlueContainer, onPrimaryContainer = Color(0xFF082E52),
    tertiary = Available, background = WarmBackground, onBackground = Ink,
    surface = WarmSurface, onSurface = Ink, surfaceContainer = Color(0xFFF0F1F1),
    surfaceContainerLow = Color(0xFFF4F4F2), surfaceContainerHigh = Color(0xFFE5E8EA),
    onSurfaceVariant = MutedInk,
)

@Composable
fun AIAssistantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
