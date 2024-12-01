package com.example.autopark.navigation

sealed class Screen(val route: String) {
    object Main : Screen("Main")
    object Login : Screen("Login")
    object Auto : Screen("Auto")
    object AutoPersonnel : Screen("Auto Personnel")
    object Routes : Screen("Routes")
    object Journal : Screen("Journal")
}