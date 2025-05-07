package com.gibraltar0123.materialapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gibraltar0123.materialapp.ui.screen.AboutScreen
import com.gibraltar0123.materialapp.ui.screen.MainScreen
import com.gibraltar0123.materialapp.util.MaterialViewModelFactory
import com.gibraltar0123.materialapp.viewmodel.MaterialViewModel


@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            val materialViewModel: MaterialViewModel = viewModel(
                factory = MaterialViewModelFactory(context)
            )
            MainScreen(navController, materialViewModel)
        }
        composable(route = Screen.About.route) {
            AboutScreen(navController)
        }
    }
}