package com.template.app.presentation.home

import com.template.app.domain.model.ExampleItem

data class HomeUiState(
    val isLoading: Boolean = false,
    val items: List<ExampleItem> = emptyList(),
    val error: String? = null
)