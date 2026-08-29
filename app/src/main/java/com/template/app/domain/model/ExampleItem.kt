package com.template.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExampleItem(
    val id: Int,
    val title: String,
    val description: String
)