package com.template.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Home : Route
    @Serializable
    data class Detail(val id: Int) : Route
}