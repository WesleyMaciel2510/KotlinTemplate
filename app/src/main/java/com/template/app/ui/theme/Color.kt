package com.template.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB0CCE8),
    onPrimary = Color(0xFF1B334B),
    primaryContainer = Color(0xFF344D65),
    onPrimaryContainer = Color(0xFFD1E4F7),
    secondary = Color(0xFFB0CCE8),
    onSecondary = Color(0xFF1B334B),
    secondaryContainer = Color(0xFF344D65),
    onSecondaryContainer = Color(0xFFD1E4F7),
    tertiary = Color(0xFFB8CDE6),
    onTertiary = Color(0xFF1E3145),
    tertiaryContainer = Color(0xFF354A5F),
    onTertiaryContainer = Color(0xFFD1E4F7),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    surface = Color(0xFF141C1F),
    onSurface = Color(0xFFE2E2E5),
    surfaceContainerHighest = Color(0xFF30373A),
    onSurfaceVariant = Color(0xFFC4C6C9),
    outline = Color(0xFF8E9093),
    background = Color(0xFF141C1F),
    onBackground = Color(0xFFE2E2E5)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1B334B),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD1E4F7),
    onPrimaryContainer = Color(0xFF001D33),
    secondary = Color(0xFF1B334B),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD1E4F7),
    onSecondaryContainer = Color(0xFF001D33),
    tertiary = Color(0xFF1E3145),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD1E4F7),
    onTertiaryContainer = Color(0xFF001D33),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    surface = Color(0xFFF8F9FA),
    onSurface = Color(0xFF1A1C1E),
    surfaceContainerHighest = Color(0xFFE0E2E5),
    onSurfaceVariant = Color(0xFF44474A),
    outline = Color(0xFF747679),
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF1A1C1E)
)

val AppDarkColorScheme = DarkColorScheme
val AppLightColorScheme = LightColorScheme