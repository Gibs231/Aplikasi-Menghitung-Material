package com.gibraltar0123.materialapp.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home_screen")
    data object About : Screen("about_screen")
    data object AddMaterial : Screen("add_material_screen")
    data object EditMaterial : Screen("edit_material_screen/{materialId}") {
        fun createRoute(materialId: Long): String {
            return "edit_material_screen/$materialId"
        }
    }
}