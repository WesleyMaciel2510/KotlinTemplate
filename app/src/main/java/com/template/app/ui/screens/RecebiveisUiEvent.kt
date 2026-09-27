package com.template.app.ui.screens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.template.app.domain.model.RecebivelFilterOption

@Stable
sealed interface RecebiveisUiEvent {
    @Immutable
    data class SearchQueryChanged(val query: String) : RecebiveisUiEvent

    @Immutable
    data class FilterSelected(val filter: RecebivelFilterOption) : RecebiveisUiEvent

    @Immutable
    data class DateRangeApplied(val startDate: String, val endDate: String) : RecebiveisUiEvent

    @Immutable
    data object Refresh : RecebiveisUiEvent
}
