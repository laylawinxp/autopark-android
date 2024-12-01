package com.example.autopark.ui.screens.journal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import com.example.autopark.dto.AutoDto
import com.example.autopark.dto.JournalDto
import com.example.autopark.dto.RoutesDto
import com.example.autopark.ui.screens.FieldText
import com.example.autopark.ui.screens.isAdmin

@Composable
fun JournalGridItem(
    journal: JournalDto,
    routes: List<RoutesDto>,
    autos: List<AutoDto>,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val route = routes.find { it.id == journal.routeId }
    val auto = autos.find { it.id == journal.autoId }

    val timeOut = if (journal.timeOut != "-") formatDate(journal.timeOut) else journal.timeOut
    val timeIn = if (journal.timeIn != "-") formatDate(journal.timeIn) else journal.timeIn

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
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
                    text = "Journal",
                    fontSize = 19.sp,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                FieldText(label = "Time Out:", value = timeOut)
                FieldText(label = "Time In:", value = timeIn)
                FieldText(
                    label = "Route:",
                    value = route?.name ?: "-"
                )
                FieldText(
                    label = "Auto:",
                    value = auto?.num ?: "-"
                )
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
}
