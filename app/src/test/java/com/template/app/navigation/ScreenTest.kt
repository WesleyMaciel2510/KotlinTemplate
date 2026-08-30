package com.template.app.navigation

import com.template.app.navigation.Screen
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import org.junit.Test
import org.junit.Assert.*

class ScreenTest {

    @Test
    fun screenHasFourDestinations() {
        val screens = Screen.values()
        assertEquals(4, screens.size)
    }

    @Test
    fun homeScreenHasCorrectProperties() {
        assertEquals("home", Screen.Home.route)
        assertEquals(Home(), Screen.Home.icon)
        assertEquals("Home", Screen.Home.contentDescription)
    }

    @Test
    fun searchScreenHasCorrectProperties() {
        assertEquals("search", Screen.Search.route)
        assertEquals(Search(), Screen.Search.icon)
        assertEquals("Search", Screen.Search.contentDescription)
    }

    @Test
    fun favoritesScreenHasCorrectProperties() {
        assertEquals("favorites", Screen.Favorites.route)
        assertEquals(Favorite(), Screen.Favorites.icon)
        assertEquals("Favorites", Screen.Favorites.contentDescription)
    }

    @Test
    fun profileScreenHasCorrectProperties() {
        assertEquals("profile", Screen.Profile.route)
        assertEquals(Person(), Screen.Profile.icon)
        assertEquals("Profile", Screen.Profile.contentDescription)
    }
}