package com.example.autopark.ui.screens.auto

import EditAutoForm
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
import com.example.autopark.dto.AutoDto
import com.example.autopark.ui.screens.AddButton
import com.example.autopark.viewModel.AutoPersonnelViewModel
import com.example.autopark.viewModel.AutoViewModel

@Composable
fun AutoScreen(viewModel: AutoViewModel, personnelViewModel: AutoPersonnelViewModel) {

    val autoList by viewModel.autos.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val autoPersonnelList by personnelViewModel.autoPersonnel.collectAsState(initial = emptyList())

    val isEditing = remember { mutableStateOf(false) }
    val editingAuto = remember { mutableStateOf<AutoDto?>(null) }
    val isAdding = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getAutos()
        personnelViewModel.getAutoPersonnel()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f)
            ) {
                items(autoList) { auto ->
                    AutoGridItem(
                        auto = auto,
                        autoPersonnelList = autoPersonnelList,
                        onEditClick = {
                            editingAuto.value = auto
                            isEditing.value = true
                        },
                        onDeleteClick = {
                            viewModel.deleteAuto(auto.id)
                        })
                }
            }

            AddButton(text = "Add New Auto") {
                isAdding.value = true
            }
        }

        if (isEditing.value && editingAuto.value != null) {
            EditAutoForm(
                auto = editingAuto.value!!,
                autoPersonnelList = autoPersonnelList,
                onSave = { updatedAuto ->
                    viewModel.updateAuto(updatedAuto.id, updatedAuto)
                    isEditing.value = false
                    editingAuto.value = null
                },
                onCancel = {
                    isEditing.value = false
                    editingAuto.value = null
                }
            )
        }

        if (isAdding.value) {
            EditAutoForm(
                auto = AutoDto(id = 0, num = "", color = "", mark = "", personalId = 0),
                autoPersonnelList = autoPersonnelList,
                onSave = { newAuto ->
                    viewModel.addAuto(newAuto)
                    isAdding.value = false
                },
                onCancel = {
                    isAdding.value = false
                }
            )
        }
    }
}
