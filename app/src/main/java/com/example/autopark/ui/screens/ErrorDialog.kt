package com.example.autopark.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(message: String, onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Error")
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            androidx.compose.material3.Button(
                onClick = onDismiss
            ) {
                Text("OK")
            }
        }
    )
}
