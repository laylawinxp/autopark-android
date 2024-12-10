package com.example.autopark.ui.screens.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.autopark.dto.AutoDto
import com.example.autopark.dto.RoutesDto
import com.example.autopark.ui.screens.EditDeleteButtons
import com.example.autopark.ui.screens.FieldBaseText
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
    val auto = if (carId != "-") (autoList.find { it.id == carId.toInt() })?.num else carId

    val currentRouteCount by routesViewModel.currentRouteCounts.collectAsState()
    val count = currentRouteCount[route.id] ?: 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FieldBaseText(value = route.name, modifier = Modifier.width(150.dp).align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(4.dp))
                FieldBaseText(value = count.toString(), modifier = Modifier.width(150.dp).align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(4.dp))
                FieldBaseText(value = timeMin, modifier = Modifier.width(150.dp).align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(4.dp))
                FieldBaseText(value = auto.toString(), modifier = Modifier.width(150.dp).align(Alignment.CenterVertically))

                if (isAdmin) {
                    EditDeleteButtons(true, onEditClick, onDeleteClick)
                }
            }
        }
    }
}
