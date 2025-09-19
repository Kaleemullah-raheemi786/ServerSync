package com.example.server_sync.ui.theme.others

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.server_sync.routes.AppDetailsScreenRoute
import com.example.server_sync.routes.AppsListScreenRoute
import com.example.server_sync.ui.theme.screens.AppDetailsScreen
import com.example.server_sync.ui.theme.screens.AppsListScreen
import com.example.server_sync.viewmodel.MainViewModel

@Composable
fun AppNavGraph(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppsListScreenRoute
    ) {
        composable<AppsListScreenRoute> {
            AppsListScreen(
                viewModel = viewModel,
                onAppClicked = { app ->
                    navController.navigate(
                        AppDetailsScreenRoute(
                            appName = app.name,
                            iconUrl = app.icon,
                            rating = app.rating.toFloat()
                        )
                    )
                }
            )
        }

        composable<AppDetailsScreenRoute> { backStackEntry ->
            val args = backStackEntry.arguments
            val appName = args?.getString("appName") ?: ""
            val iconUrl = args?.getString("iconUrl") ?: ""
            val rating = args?.getFloat("rating") ?: 0f

            AppDetailsScreen(
                appName = appName,
                iconUrl = iconUrl,
                rating = rating,
                onBack = { navController.popBackStack() }
            )
        }

    }
}

