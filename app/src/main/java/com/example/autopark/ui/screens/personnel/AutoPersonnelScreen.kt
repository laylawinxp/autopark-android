package com.example.autopark.ui.screens.personnel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.autopark.dto.AutoPersonnelDto
import com.example.autopark.ui.screens.AddButton
import com.example.autopark.viewModel.AutoPersonnelViewModel

@Composable
fun AutoPersonnelScreen(viewModel: AutoPersonnelViewModel) {
    val personnelList by viewModel.autoPersonnel.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()

    val isEditing = remember { mutableStateOf(false) }
    val editingPersonnel = remember { mutableStateOf<AutoPersonnelDto?>(null) }
    val isAdding = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getAutoPersonnel()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f)
                ) {
                    items(personnelList) { personnel ->
                        AutoPersonnelGridItem(
                            personnel = personnel,
                            onEditClick = {
                                editingPersonnel.value = personnel
                                isEditing.value = true
                            },
                            onDeleteClick = {
                                viewModel.deletePersonnel(personnel.id)
                            }
                        )
                    }
                }

                AddButton(text = "Add New Personnel") {
                    isAdding.value = true
                }
            }

            if (isEditing.value && editingPersonnel.value != null) {
                AutoPersonnelForm(
                    personnel = editingPersonnel.value!!,
                    onSave = { updatedPersonnel: AutoPersonnelDto ->
                        viewModel.updatePersonnel(updatedPersonnel.id, updatedPersonnel)
                        isEditing.value = false
                        editingPersonnel.value = null
                    },
                    onCancel = {
                        isEditing.value = false
                        editingPersonnel.value = null
                    }
                )
            }

            if (isAdding.value) {
                AutoPersonnelForm(
                    personnel = AutoPersonnelDto(id = 0, firstName = "", lastName = "", fatherName = ""),
                    onSave = { newPersonnel: AutoPersonnelDto ->
                        viewModel.addPersonnel(newPersonnel)
                        isAdding.value = false
                    },
                    onCancel = {
                        isAdding.value = false
                    }
                )
            }
        }
    }
}
