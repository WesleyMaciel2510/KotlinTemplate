package com.template.app.ui.screens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.template.app.domain.model.ActivityEvent
import com.template.app.domain.model.KpiMetric
import com.template.app.domain.model.MonthlyTrendPoint
import com.template.app.domain.model.TopClient

@Stable
sealed interface HomeUiState {
    @Immutable
    data object Loading : HomeUiState

    @Immutable
    data object Empty : HomeUiState

    @Immutable
    data class Success(
        val userName: String,
        val kpis: List<KpiMetric>,
        val monthlyReceipts: List<MonthlyTrendPoint>,
        val topClients: List<TopClient>,
        val recentActivities: List<ActivityEvent>
    ) : HomeUiState

    @Immutable
    data class Error(val message: String) : HomeUiState
}
