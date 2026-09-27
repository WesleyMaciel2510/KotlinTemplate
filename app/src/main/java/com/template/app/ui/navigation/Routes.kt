package com.template.app.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

@Serializable
data object SearchRoute

@Serializable
data object FinanceiroRoute

@Serializable
data object ProfileRoute

@Serializable
data class DetailsRoute(val id: String)

@Serializable
data object QrCodeRoute

@Serializable
data object RecebiveisListRoute

@Serializable
data class RecebivelDetailRoute(val id: String)
