package com.template.app.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemePreferenceDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val themeKey = intPreferencesKey("theme_preference")

    /** Emits the stored [ThemePreference]; defaults to [ThemePreference.System] if unset. */
    val themeFlow: Flow<ThemePreference> = dataStore.data.map { prefs ->
        val ordinal = prefs[themeKey] ?: ThemePreference.System.ordinal
        ThemePreference.entries.getOrElse(ordinal) { ThemePreference.System }
    }

    /** Persists [pref] across process death and restarts. */
    suspend fun setTheme(pref: ThemePreference) {
        dataStore.edit { prefs -> prefs[themeKey] = pref.ordinal }
    }
}
