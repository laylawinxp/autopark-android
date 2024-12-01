package com.example.autopark.ui.screens.personnel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autopark.dto.AutoPersonnelDto
import com.example.autopark.ui.screens.FieldText
import com.example.autopark.ui.screens.isAdmin

@Composable
fun AutoPersonnelGridItem(
    personnel: AutoPersonnelDto,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Personnel",
                fontSize = 19.sp,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            FieldText(label = "First Name:", value = personnel.firstName)
            FieldText(label = "Last Name:", value = personnel.lastName)
            FieldText(label = "Father Name:", value = personnel.fatherName)

            if (isAdmin) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Button(onClick = onEditClick, modifier = Modifier.fillMaxWidth()) {
                        Text("Edit")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onDeleteClick, modifier = Modifier.fillMaxWidth()) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}
