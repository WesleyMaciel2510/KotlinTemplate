package com.template.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import com.template.app.ui.navigation.FinanceiroRoute
import com.template.app.ui.navigation.HomeRoute
import com.template.app.ui.navigation.ProfileRoute
import com.template.app.ui.navigation.SearchRoute
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
        assertEquals(HomeRoute, Screen.Home.route)
        assertEquals(Icons.Default.Home, Screen.Home.icon)
        assertEquals("Home", Screen.Home.contentDescription)
    }

    @Test
    fun searchScreenHasCorrectProperties() {
        assertEquals(SearchRoute, Screen.Search.route)
        assertEquals(Icons.Default.Search, Screen.Search.icon)
        assertEquals("Search", Screen.Search.contentDescription)
    }

    @Test
    fun financeiroScreenHasCorrectProperties() {
        assertEquals(FinanceiroRoute, Screen.Financeiro.route)
        assertEquals(Icons.Default.AccountBalanceWallet, Screen.Financeiro.icon)
        assertEquals("Financeiro", Screen.Financeiro.contentDescription)
    }

    @Test
    fun profileScreenHasCorrectProperties() {
        assertEquals(ProfileRoute, Screen.Profile.route)
        assertEquals(Icons.Default.Person, Screen.Profile.icon)
        assertEquals("Profile", Screen.Profile.contentDescription)
    }
}