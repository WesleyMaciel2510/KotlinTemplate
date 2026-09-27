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
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.template.app.financeiro.presentation.FinanceiroScreen
import com.template.app.navigation.Screen
import com.template.app.qrcode.QrCodeReaderScreen
import com.template.app.ui.navigation.DetailsRoute
import com.template.app.ui.navigation.FinanceiroRoute
import com.template.app.ui.navigation.HomeRoute
import com.template.app.ui.navigation.ProfileRoute
import com.template.app.ui.navigation.QrCodeRoute
import com.template.app.ui.navigation.RecebivelDetailRoute
import com.template.app.ui.navigation.RecebiveisListRoute
import com.template.app.ui.navigation.SearchRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ) {
                Screen.entries.forEach { screen ->
                    val isSelected = currentDestination?.hasRoute(screen.route::class) == true
                    NavigationBarItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.contentDescription) },
                        label = { Text(text = screen.contentDescription, style = MaterialTheme.typography.labelMedium) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            composable<HomeRoute> {
                HomeContentScreen(
                    onNavigateToRecebiveis = { navController.navigate(RecebiveisListRoute) }
                )
            }
            composable<SearchRoute> { SearchContentScreen() }
            composable<FinanceiroRoute> { FinanceiroScreen() }
            composable<ProfileRoute> { ProfileContentScreen(onNavigateToQrScanner = { navController.navigate(QrCodeRoute) }) }
            composable<DetailsRoute> { backStackEntry ->
                val route: DetailsRoute = backStackEntry.toRoute()
                DetailsScreen(itemId = route.id, onNavigateBack = { navController.popBackStack() })
            }
            composable<QrCodeRoute> {
                QrCodeReaderScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable<RecebiveisListRoute> {
                RecebiveisListScreen(onRecebivelClick = { id -> navController.navigate(RecebivelDetailRoute(id)) })
            }
            composable<RecebivelDetailRoute> {
                RecebivelDetailScreen(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun MainScreenPreview() {
    com.template.app.ui.theme.TemplateAppTheme { MainScreen() }
}
