package com.example.autopark.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FieldText(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 4.dp)) {
        Text(
            text = label,
            fontSize = 17.sp,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun FieldBaseText(value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(bottom = 4.dp)) {
        Text(
            text = value,
            fontSize = 16.sp,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


@Composable
fun FieldLabelText(label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(2.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 17.sp,
            modifier = Modifier
                .padding(8.dp),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
