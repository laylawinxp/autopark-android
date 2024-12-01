package com.example.autopark.ui.screens.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.autopark.dto.RoutesDto
import com.example.autopark.ui.screens.AddButton
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

    Column(modifier = Modifier.padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f)
            ) {
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

            AddButton(text = "Add New Route") {
                isAdding.value = true
            }
        }

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
    }
}
