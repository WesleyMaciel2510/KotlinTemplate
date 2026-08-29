package com.template.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExampleItemDto(
    val id: Int,
    val title: String,
    val description: String
)