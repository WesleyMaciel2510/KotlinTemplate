package com.template.app.ui.theme

import androidx.compose.runtime.Composable
import com.template.app.data.preferences.ThemePreference

/** Compatibility overload for previews that select a boolean theme directly. */
@Composable
fun TemplateAppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    TemplateAppTheme(
        themePreference = if (darkTheme) ThemePreference.Dark else ThemePreference.Light,
        content = content
    )
}
