package com.gibraltar0123.materialapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gibraltar0123.materialapp.ui.screen.AboutScreen
import com.gibraltar0123.materialapp.ui.screen.MainScreen
import com.gibraltar0123.materialapp.ui.screen.MaterialFormScreen
import com.gibraltar0123.materialapp.viewmodel.MaterialViewModel
import com.gibraltar0123.materialapp.util.MaterialViewModelFactory

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current


    val materialViewModel: MaterialViewModel = viewModel(
        factory = MaterialViewModelFactory(context)
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController, materialViewModel)
        }

        composable(route = Screen.About.route) {
            AboutScreen(navController)
        }

        composable(route = Screen.AddMaterial.route) {
            MaterialFormScreen(navController, materialViewModel)
        }

        composable(
            route = Screen.EditMaterial.route,
            arguments = listOf(
                navArgument("materialId") {
                    type = NavType.LongType
                }
            )
        ) { entry ->
            val materialId = entry.arguments?.getLong("materialId") ?: 0L
            MaterialFormScreen(navController, materialViewModel, materialId)
        }
    }
}