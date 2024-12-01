package com.example.autopark.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.autopark.navigation.AppNavHost
import com.example.autopark.ui.DrawerBody
import com.example.autopark.viewModel.AutoPersonnelViewModel
import com.example.autopark.viewModel.AutoViewModel
import com.example.autopark.viewModel.JournalViewModel
import com.example.autopark.viewModel.RoutesViewModel
import com.example.autopark.viewModel.UsersViewModel

@Composable
fun MainScreen(
    autoViewModel: AutoViewModel,
    autoPersonnelViewModel: AutoPersonnelViewModel,
    routesViewModel: RoutesViewModel,
    journalViewModel: JournalViewModel,
    usersViewModel: UsersViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Autopark App") }
            )
        },
        drawerContent = {
            DrawerBody(navController = navController)
        },
        content = { padding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(padding),
                autoViewModel = autoViewModel,
                autoPersonnelViewModel = autoPersonnelViewModel,
                routesViewModel = routesViewModel,
                journalViewModel = journalViewModel,
                usersViewModel = usersViewModel
            )
        }
    )
}
