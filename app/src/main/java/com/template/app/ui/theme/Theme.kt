package com.template.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.template.app.data.preferences.ThemePreference

/** Root theme composable for Material 3 and the app-specific color tokens. */
@Composable
fun TemplateAppTheme(
    themePreference: ThemePreference = ThemePreference.System,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (themePreference) {
        ThemePreference.Dark -> true
        ThemePreference.Light -> false
        ThemePreference.System -> systemDark
    }

    val colorScheme = if (darkTheme) appDarkColorScheme else appLightColorScheme
    val appColors = if (darkTheme) AppColors.dark else AppColors.light
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = !darkTheme
        )
        systemUiController.setNavigationBarColor(
            color = colorScheme.surface,
            darkIcons = !darkTheme
        )
    }

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = appTypography,
            shapes = appShapes,
            content = content
        )
    }
}

@Composable
fun TemplateAppSurface(
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit
) {
    Surface(modifier = modifier, color = color, content = content)
}
