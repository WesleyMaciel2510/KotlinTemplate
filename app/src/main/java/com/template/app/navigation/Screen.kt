package com.template.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.template.app.ui.navigation.FinanceiroRoute
import com.template.app.ui.navigation.HomeRoute
import com.template.app.ui.navigation.ProfileRoute
import com.template.app.ui.navigation.SearchRoute

enum class Screen(
    val route: Any,
    val icon: ImageVector,
    val contentDescription: String
) {
    Home(HomeRoute, Icons.Filled.Home, "Home"),
    Search(SearchRoute, Icons.Filled.Search, "Search"),
    Financeiro(FinanceiroRoute, Icons.Filled.AccountBalanceWallet, "Financeiro"),
    Profile(ProfileRoute, Icons.Filled.Person, "Profile");
}