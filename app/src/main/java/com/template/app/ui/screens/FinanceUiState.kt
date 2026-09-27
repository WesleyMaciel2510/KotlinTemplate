package com.template.app.ui.screens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.template.app.domain.model.MonthlyTrendPoint
import com.template.app.domain.model.PaymentMethodShare
import com.template.app.domain.model.PaymentTransaction

@Stable
sealed interface FinanceUiState {
    @Immutable
    data object Loading : FinanceUiState

    @Immutable
    data object Empty : FinanceUiState

    @Immutable
    data class Success(
        val totalReceived: String,
        val totalToReceive: String,
        val netBalance: String,
        val receivedRatio: Float,
        val paymentMethodShares: List<PaymentMethodShare>,
        val revenueTrend: List<MonthlyTrendPoint>,
        val delinquencyTrend: List<MonthlyTrendPoint>,
        val recentTransactions: List<PaymentTransaction>
    ) : FinanceUiState

    @Immutable
    data class Error(val message: String) : FinanceUiState
}
