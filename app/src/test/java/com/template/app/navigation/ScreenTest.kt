package com.template.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import org.junit.Assert.assertEquals
import org.junit.Test

class ScreenTest {

    @Test
    fun screenHasFourDestinations() {
        val screens = Screen.entries
        assertEquals(4, screens.size)
    }

    @Test
    fun homeScreenHasCorrectProperties() {
        assertEquals("home", Screen.Home.route)
        assertEquals(Icons.Default.Home, Screen.Home.icon)
        assertEquals("Home", Screen.Home.contentDescription)
    }

    @Test
    fun searchScreenHasCorrectProperties() {
        assertEquals("search", Screen.Search.route)
        assertEquals(Icons.Default.Search, Screen.Search.icon)
        assertEquals("Search", Screen.Search.contentDescription)
    }

    @Test
    fun favoritesScreenHasCorrectProperties() {
        assertEquals("favorites", Screen.Favorites.route)
        assertEquals(Icons.Default.Favorite, Screen.Favorites.icon)
        assertEquals("Favorites", Screen.Favorites.contentDescription)
    }

    @Test
    fun profileScreenHasCorrectProperties() {
        assertEquals("profile", Screen.Profile.route)
        assertEquals(Icons.Default.Person, Screen.Profile.icon)
        assertEquals("Profile", Screen.Profile.contentDescription)
    }
}