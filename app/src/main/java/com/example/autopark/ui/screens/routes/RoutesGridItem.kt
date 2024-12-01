package com.example.autopark.ui.screens.routes

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autopark.dto.AutoDto
import com.example.autopark.dto.RoutesDto
import com.example.autopark.ui.screens.FieldText
import com.example.autopark.ui.screens.isAdmin
import com.example.autopark.viewModel.RoutesViewModel

@Composable
fun RoutesGridItem(
    route: RoutesDto,
    autoList: List<AutoDto>,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    routesViewModel: RoutesViewModel
) {
    val timeAndCar by routesViewModel.timeAndCar.collectAsState()
    val time = timeAndCar[route.id]?.first ?: "-"
    val timeMin = if (time != "-") ((time.toDouble() / 60.0).toString() + " min") else time
    val carId = timeAndCar[route.id]?.second ?: "-"
    val auto =
        if (carId != "-") (autoList.find { auto -> auto.id == carId.toInt() })?.num else carId

    val currentRouteCount by routesViewModel.currentRouteCounts.collectAsState()
    val count = currentRouteCount[route.id] ?: 0

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
                text = "Route",
                fontSize = 19.sp,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            FieldText(label = "Route Name:", value = route.name)
            FieldText(label = "Cars in route:", value = count.toString())
            FieldText(label = "Shortest time:", value = timeMin)
            FieldText(label = "Fastest car num:", value = auto.toString())

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
