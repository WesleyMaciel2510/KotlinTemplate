package com.template.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val route: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    Home("home", Icons.Filled.Home, "Home"),
    Search("search", Icons.Filled.Search, "Search"),
    Favorites("favorites", Icons.Filled.Favorite, "Favorites"),
    Profile("profile", Icons.Filled.Person, "Profile");
}