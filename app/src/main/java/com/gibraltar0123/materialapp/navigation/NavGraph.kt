package com.gibraltar0123.materialapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gibraltar0123.materialapp.ui.screen.AboutScreen
import com.gibraltar0123.materialapp.ui.screen.MainScreen
import com.gibraltar0123.materialapp.viewmodel.MaterialViewModel


@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            val mainViewModel: MaterialViewModel = viewModel() // Pass ViewModel to MainScreen
            MainScreen(navController, MaterialViewModel) // Pass the ViewModel here
        }
        composable(route = Screen.About.route) {
            AboutScreen(navController) // No ViewModel needed here
        }
    }
}
