package com.template.app.financeiro.presentation

import com.template.app.financeiro.domain.FinanceiroSummary

data class FinanceiroUiState(
    val data: FinanceiroSummary? = null,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val refreshErrorMessage: String? = null
)
