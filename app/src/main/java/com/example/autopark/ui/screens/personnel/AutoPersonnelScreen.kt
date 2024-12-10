package com.example.autopark.ui.screens.personnel

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
import com.example.autopark.dto.AutoPersonnelDto
import com.example.autopark.ui.DownloadReportButton
import com.example.autopark.ui.screens.AddButton
import com.example.autopark.ui.screens.EditDeleteButtons
import com.example.autopark.ui.screens.ErrorDialog
import com.example.autopark.ui.screens.FieldLabelText
import com.example.autopark.ui.screens.isAdmin
import com.example.autopark.viewModel.AutoPersonnelViewModel

@Composable
fun AutoPersonnelScreen(viewModel: AutoPersonnelViewModel) {
    val personnelList by viewModel.autoPersonnel.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val actionError by viewModel.actionError.collectAsState()

    val isEditing = remember { mutableStateOf(false) }
    val editingPersonnel = remember { mutableStateOf<AutoPersonnelDto?>(null) }
    val isAdding = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getAutoPersonnel()
    }

    Column(modifier = Modifier.padding(8.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
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
                    personnel = AutoPersonnelDto(
                        id = 0,
                        firstName = "",
                        lastName = "",
                        fatherName = ""
                    ),
                    onSave = { newPersonnel: AutoPersonnelDto ->
                        viewModel.addPersonnel(newPersonnel)
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
                FieldLabelText("First Name", Modifier.width(120.dp).align(Alignment.CenterVertically))
                FieldLabelText("Father Name", Modifier.width(150.dp).align(Alignment.CenterVertically))
                FieldLabelText("Last Name", Modifier.width(120.dp).align(Alignment.CenterVertically))

                if (isAdmin) {
                    EditDeleteButtons(false, {}, {})
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
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

            Row(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isAdmin) {
                    AddButton(text = "Add New Personnel") {
                        isAdding.value = true
                    }
                }

                DownloadReportButton(
                    list = personnelList,
                    context = LocalContext.current
                )
            }
        }

        actionError?.let {
            ErrorDialog(
                message = it,
                onDismiss = {
                    viewModel.clearError()
                }
            )
        }
    }
}

