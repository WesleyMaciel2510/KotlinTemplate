package com.template.app.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.template.app.navigation.Screen
import com.template.app.ui.navigation.QrCodeRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val selectedDestination = remember { mutableStateOf(Screen.Home) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Kotlin", fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ) {
                val screens = Screen.values()
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.contentDescription) },
                        label = { Text(text = screen.contentDescription, fontSize = 12.sp) },
                        selected = selectedDestination.value == screen,
                        onClick = {
                            selectedDestination.value = screen
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            composable(route = Screen.Home.route) {
                HomeContentScreen(
                    onNavigateToQrScanner = {
                        navController.navigate("qrcode")
                    }
                )
            }
            composable(route = Screen.Search.route) {
                SearchContentScreen()
            }
            composable(route = Screen.Favorites.route) {
                FavoritesContentScreen()
            }
            composable(route = Screen.Profile.route) {
                ProfileContentScreen()
            }
            composable(route = "qrcode") {
                com.template.app.qrcode.QrCodeReaderScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun MainScreenPreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        MainScreen()
    }
}