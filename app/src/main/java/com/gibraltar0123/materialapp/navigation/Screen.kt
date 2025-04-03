package com.gibraltar0123.materialapp.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("mainScreen")
}