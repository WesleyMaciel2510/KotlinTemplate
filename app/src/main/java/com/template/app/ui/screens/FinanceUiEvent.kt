package com.template.app.ui.screens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface FinanceUiEvent {
    @Immutable
    data object Refresh : FinanceUiEvent
}
