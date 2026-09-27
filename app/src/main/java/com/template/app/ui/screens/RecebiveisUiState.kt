package com.template.app.ui.screens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.template.app.domain.model.KpiMetric
import com.template.app.domain.model.RecebivelFilterOption
import com.template.app.domain.model.RecebivelItem

@Stable
sealed interface RecebiveisUiState {
    @Immutable
    data object Loading : RecebiveisUiState

    @Immutable
    data object Empty : RecebiveisUiState

    @Immutable
    data class Success(
        val totalToReceive: String,
        val totalOverdue: String,
        val kpis: List<KpiMetric>,
        val items: List<RecebivelItem>,
        val searchQuery: String,
        val selectedFilter: RecebivelFilterOption,
        val dateRangeText: String = "Período: Set/2026"
    ) : RecebiveisUiState

    @Immutable
    data class Error(val message: String) : RecebiveisUiState
}
