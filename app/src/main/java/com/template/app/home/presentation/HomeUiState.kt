package com.template.app.home.presentation

import com.template.app.home.domain.HomeOverview

data class HomeUiState(
    val data: HomeOverview? = null,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val refreshErrorMessage: String? = null
)
