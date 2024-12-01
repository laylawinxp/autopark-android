package com.example.autopark.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.autopark.ui.screens.LoginScreen
import com.example.autopark.ui.screens.MainScreen
import com.example.autopark.ui.screens.auto.AutoScreen
import com.example.autopark.ui.screens.journal.JournalScreen
import com.example.autopark.ui.screens.personnel.AutoPersonnelScreen
import com.example.autopark.ui.screens.routes.RoutesScreen
import com.example.autopark.viewModel.AutoPersonnelViewModel
import com.example.autopark.viewModel.AutoViewModel
import com.example.autopark.viewModel.JournalViewModel
import com.example.autopark.viewModel.RoutesViewModel
import com.example.autopark.viewModel.UsersViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    autoViewModel: AutoViewModel,
    autoPersonnelViewModel: AutoPersonnelViewModel,
    routesViewModel: RoutesViewModel,
    journalViewModel: JournalViewModel,
    usersViewModel: UsersViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auto.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                usersViewModel = usersViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                autoViewModel = autoViewModel,
                autoPersonnelViewModel = autoPersonnelViewModel,
                routesViewModel = routesViewModel,
                journalViewModel = journalViewModel,
                usersViewModel = usersViewModel
            )
        }

        composable(Screen.Auto.route) {
            AutoScreen(autoViewModel, autoPersonnelViewModel)
        }

        composable(Screen.AutoPersonnel.route) {
            AutoPersonnelScreen(autoPersonnelViewModel)
        }

        composable(Screen.Routes.route) {
            RoutesScreen(routesViewModel, autoViewModel)
        }

        composable(Screen.Journal.route) {
            JournalScreen(journalViewModel, routesViewModel, autoViewModel)
        }
    }
}
