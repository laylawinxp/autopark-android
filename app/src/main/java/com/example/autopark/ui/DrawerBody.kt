package com.example.autopark.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.autopark.navigation.Screen

@Composable
fun DrawerBody(navController: NavHostController) {
    val items = listOf(
        Screen.Auto,
        Screen.AutoPersonnel,
        Screen.Routes,
        Screen.Journal
    )
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        items.forEachIndexed { index, screen ->
            TextButton(
                onClick = { navController.navigate(screen.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = screen.route)
            }

            if (index < items.size - 1) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}
