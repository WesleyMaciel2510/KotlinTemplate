package com.template.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.template.app.di.AppContainer
import com.template.app.presentation.home.HomeScreen
import com.template.app.domain.model.ExampleItem

@Composable
fun AppNavHost(container: AppContainer) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.Home.route) {
        composable(route = Route.Home.route) {
            HomeScreen(
                viewModel = container.createHomeViewModel(),
                onItemClick = { item: ExampleItem ->
                    navController.navigate(Route.Detail(item.id).route)
                }
            )
        }
        composable(
            route = "detail/{id}"
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            DetailScreen(itemId = id)
        }
    }
}

@Composable
fun DetailScreen(itemId: Int) {
    androidx.compose.material3.Text(text = "Detail Screen for Item ID: $itemId", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
}