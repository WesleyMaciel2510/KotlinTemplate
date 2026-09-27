package com.template.app.data.preferences

/**
 * Represents the user's explicit theme preference.
 *
 * - [System]: follow the device system setting (default)
 * - [Light]: always use the light color scheme
 * - [Dark]: always use the dark color scheme
 */
enum class ThemePreference {
    System,
    Light,
    Dark;
}
