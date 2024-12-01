package com.example.autopark.ui.screens.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autopark.dto.AutoDto
import com.example.autopark.dto.JournalDto
import com.example.autopark.dto.RoutesDto

@Composable
fun EditJournalForm(
    journal: JournalDto,
    routeList: List<RoutesDto>,
    autoList: List<AutoDto>,
    onSave: (JournalDto) -> Unit,
    onCancel: () -> Unit
) {
    var timeOut by remember { mutableStateOf(journal.timeOut) }
    var timeIn by remember { mutableStateOf(journal.timeIn) }
    var routeId by remember { mutableStateOf(journal.routeId.toString()) }
    var autoId by remember { mutableStateOf(journal.autoId.toString()) }

    var expandedRoute by remember { mutableStateOf(false) }
    var expandedAuto by remember { mutableStateOf(false) }
    var selectedRouteName by remember { mutableStateOf("") }
    var selectedAutoName by remember { mutableStateOf("") }

    fun onRouteSelected(route: RoutesDto) {
        selectedRouteName = route.name
        routeId = route.id.toString()
        expandedRoute = false
    }

    fun onAutoSelected(auto: AutoDto) {
        selectedAutoName = auto.num
        autoId = auto.id.toString()
        expandedAuto = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Enter Journal Details",
                    fontSize = 19.sp,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                TextField(
                    value = timeOut,
                    onValueChange = { timeOut = it },
                    label = { Text("Time Out: dd:mm:yyyy HH:mm:ss") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                TextField(
                    value = timeIn,
                    onValueChange = { timeIn = it },
                    label = { Text("Time In: dd:mm:yyyy HH:mm:ss") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = selectedRouteName,
                            onValueChange = { },
                            label = { Text("Route") },
                            readOnly = true,
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.Transparent)
                                .clickable { expandedRoute = !expandedRoute }
                                .padding(16.dp)
                        )

                        IconButton(
                            onClick = { expandedRoute = !expandedRoute },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = if (expandedRoute) android.R.drawable.arrow_down_float else android.R.drawable.arrow_up_float),
                                contentDescription = "Toggle Route Dropdown"
                            )
                        }
                    }

                    if (routeList.isNotEmpty() && expandedRoute) {
                        DropdownMenu(
                            expanded = expandedRoute,
                            onDismissRequest = { expandedRoute = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopStart)
                                .padding(top = 8.dp)
                        ) {
                            routeList.forEach { route ->
                                DropdownMenuItem(
                                    text = { Text(route.name) },
                                    onClick = { onRouteSelected(route) }
                                )
                            }
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = selectedAutoName,
                            onValueChange = { },
                            label = { Text("Auto") },
                            readOnly = true,
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.Transparent)
                                .clickable { expandedAuto = !expandedAuto }
                                .padding(16.dp)
                        )

                        IconButton(
                            onClick = { expandedAuto = !expandedAuto },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = if (expandedAuto) android.R.drawable.arrow_down_float else android.R.drawable.arrow_up_float),
                                contentDescription = "Toggle Auto Dropdown"
                            )
                        }
                    }

                    if (autoList.isNotEmpty() && expandedAuto) {
                        DropdownMenu(
                            expanded = expandedAuto,
                            onDismissRequest = { expandedAuto = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopStart)
                                .padding(top = 8.dp)
                        ) {
                            autoList.forEach { auto ->
                                DropdownMenuItem(
                                    text = { Text(auto.num) },
                                    onClick = { onAutoSelected(auto) }
                                )
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Button(onClick = {
                        onSave(
                            JournalDto(
                                id = journal.id,
                                timeOut = formatDateToSend(timeOut),
                                timeIn = formatDateToSend(timeIn),
                                routeId = routeId.toIntOrNull() ?: journal.routeId,
                                autoId = autoId.toIntOrNull() ?: journal.autoId
                            )
                        )
                    }) {
                        Text("Save")
                    }
                    Button(onClick = onCancel) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
