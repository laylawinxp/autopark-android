package com.example.autopark.ui.screens.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.autopark.dto.RoutesDto
import com.example.autopark.ui.DownloadReportButton
import com.example.autopark.ui.screens.AddButton
import com.example.autopark.ui.screens.EditDeleteButtons
import com.example.autopark.ui.screens.FieldLabelText
import com.example.autopark.ui.screens.isAdmin
import com.example.autopark.viewModel.AutoViewModel
import com.example.autopark.viewModel.RoutesViewModel

@Composable
fun RoutesScreen(viewModel: RoutesViewModel, autoViewModel: AutoViewModel) {
    val routesList by viewModel.routes.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val autoList by autoViewModel.autos.collectAsState(initial = emptyList())

    val isAdding = remember { mutableStateOf(false) }
    val isEditing = remember { mutableStateOf(false) }
    val editingRoute = remember { mutableStateOf<RoutesDto?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getRoutes()
    }

    Column(modifier = Modifier.padding(8.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {

            if (isEditing.value && editingRoute.value != null) {
                EditRouteForm(
                    route = editingRoute.value!!,
                    onSave = { updatedRoute ->
                        updatedRoute.id.let { viewModel.updateRoute(it, updatedRoute) }
                        isEditing.value = false
                        editingRoute.value = null
                    },
                    onCancel = {
                        isEditing.value = false
                        editingRoute.value = null
                    }
                )
            }

            if (isAdding.value) {
                EditRouteForm(
                    route = RoutesDto(id = 0, name = ""),
                    onSave = { newRoute ->
                        viewModel.addRoute(newRoute)
                        isAdding.value = false
                    },
                    onCancel = {
                        isAdding.value = false
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FieldLabelText("Route Name", modifier = Modifier.width(150.dp).align(Alignment.CenterVertically))
                FieldLabelText("Now in route", modifier = Modifier.width(150.dp).align(Alignment.CenterVertically))
                FieldLabelText("Shortest time", modifier = Modifier.width(150.dp).align(Alignment.CenterVertically))
                FieldLabelText("Fastest auto", modifier = Modifier.width(150.dp).align(Alignment.CenterVertically))

                if (isAdmin) {
                    EditDeleteButtons(false, {}, {})
                }
            }

            LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = Modifier.weight(1f)) {
                items(routesList) { route ->
                    RoutesGridItem(
                        route = route,
                        autoList = autoList,
                        onEditClick = {
                            editingRoute.value = route
                            isEditing.value = true
                        },
                        onDeleteClick = {
                            route.id.let { viewModel.deleteRoute(it) }
                        },
                        routesViewModel = viewModel
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isAdmin) {
                    AddButton(text = "Add New Route") {
                        isAdding.value = true
                    }
                }

                DownloadReportButton(
                    list = routesList,
                    context = LocalContext.current
                )
            }
        }
    }
}
