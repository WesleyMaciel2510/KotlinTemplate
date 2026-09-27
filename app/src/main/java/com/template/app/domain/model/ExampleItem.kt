package com.template.app.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class ExampleItem(
    val id: Int,
    val title: String,
    val description: String
)
