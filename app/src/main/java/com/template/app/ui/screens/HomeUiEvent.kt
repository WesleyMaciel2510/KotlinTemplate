package com.template.app.ui.screens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface HomeUiEvent {
    @Immutable
    data object Refresh : HomeUiEvent
}
