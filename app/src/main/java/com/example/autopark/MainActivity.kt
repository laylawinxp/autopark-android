package com.example.autopark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.autopark.ui.screens.LoginScreen
import com.example.autopark.ui.screens.MainScreen
import com.example.autopark.viewModel.AutoPersonnelViewModel
import com.example.autopark.viewModel.AutoViewModel
import com.example.autopark.viewModel.JournalViewModel
import com.example.autopark.viewModel.RoutesViewModel
import com.example.autopark.viewModel.UsersViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val autoViewModel = ViewModelProvider(this).get(AutoViewModel::class.java)
            val autoPersonnelViewModel =
                ViewModelProvider(this).get(AutoPersonnelViewModel::class.java)
            val routesViewModel = ViewModelProvider(this).get(RoutesViewModel::class.java)
            val journalViewModel = ViewModelProvider(this).get(JournalViewModel::class.java)
            val usersViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)

            autoViewModel.getAutos()
            autoPersonnelViewModel.getAutoPersonnel()
            routesViewModel.getRoutes()
            journalViewModel.getJournals()
            usersViewModel.getUsers()

            val navController = rememberNavController()

            val isLoggedIn = remember { mutableStateOf(false) }

            if (isLoggedIn.value) {
                MainScreen(
                    autoViewModel,
                    autoPersonnelViewModel,
                    routesViewModel,
                    journalViewModel,
                    usersViewModel
                )
            } else {
                LoginScreen(navController = navController, usersViewModel, onLoginSuccess = {
                    isLoggedIn.value = true
                })
            }
        }
    }
}
