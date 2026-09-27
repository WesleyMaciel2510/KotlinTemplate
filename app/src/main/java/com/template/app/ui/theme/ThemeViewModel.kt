package com.template.app.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.app.data.preferences.ThemePreference
import com.template.app.data.preferences.ThemePreferenceDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeDataStore: ThemePreferenceDataStore
) : ViewModel() {

    /** The current theme preference, collected from DataStore. */
    val themeState: StateFlow<ThemePreference> = themeDataStore.themeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ThemePreference.System
        )

    fun setTheme(pref: ThemePreference) {
        viewModelScope.launch { themeDataStore.setTheme(pref) }
    }
}
