package com.example.autopark.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.autopark.R

@Composable
fun AddButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray
        )
    ) {
        Text(text = text)
    }
}

@Composable
fun EditDeleteButtons(
    enabled: Boolean = true,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(30.dp)
    ) {
        IconButton(
            onClick = onEditClick,
            enabled = enabled,
            modifier = Modifier.alpha(if (enabled) 1f else 0f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "Edit",
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        IconButton(
            onClick = onDeleteClick,
            enabled = enabled,
            modifier = Modifier.alpha(if (enabled) 1f else 0f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "Delete",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
