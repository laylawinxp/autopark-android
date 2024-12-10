package com.example.autopark.ui.screens.journal

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
import com.example.autopark.dto.JournalDto
import com.example.autopark.ui.DownloadReportButton
import com.example.autopark.ui.screens.AddButton
import com.example.autopark.ui.screens.EditDeleteButtons
import com.example.autopark.ui.screens.ErrorDialog
import com.example.autopark.ui.screens.FieldLabelText
import com.example.autopark.ui.screens.isAdmin
import com.example.autopark.viewModel.AutoViewModel
import com.example.autopark.viewModel.JournalViewModel
import com.example.autopark.viewModel.RoutesViewModel

@Composable
fun JournalScreen(
    viewModel: JournalViewModel,
    routeViewModel: RoutesViewModel,
    autoViewModel: AutoViewModel
) {
    val journals by viewModel.journals.collectAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val routes by routeViewModel.routes.collectAsState(emptyList())
    val autos by autoViewModel.autos.collectAsState(emptyList())
    val actionError by viewModel.actionError.collectAsState()

    val isAdding = remember { mutableStateOf(false) }
    val isEditing = remember { mutableStateOf(false) }
    val editingJournal = remember { mutableStateOf<JournalDto?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getJournals()
        routeViewModel.getRoutes()
        autoViewModel.getAutos()
    }

    Column(modifier = Modifier.padding(8.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            if (isEditing.value && editingJournal.value != null) {
                EditJournalForm(
                    journal = JournalDto(
                        editingJournal.value!!.id,
                        formatDate(editingJournal.value!!.timeOut),
                        formatDate(editingJournal.value!!.timeIn),
                        editingJournal.value!!.routeId,
                        editingJournal.value!!.autoId
                    ),
                    routeList = routes,
                    autoList = autos,
                    onSave = { updatedJournal ->
                        viewModel.updateJournal(updatedJournal.id, updatedJournal)
                        isEditing.value = false
                        editingJournal.value = null
                    },
                    onCancel = {
                        isEditing.value = false
                        editingJournal.value = null
                    }
                )
            }

            if (isAdding.value) {
                EditJournalForm(
                    journal = JournalDto(
                        id = 0,
                        timeOut = "",
                        timeIn = "",
                        routeId = 0,
                        autoId = 0
                    ),
                    routeList = routes,
                    autoList = autos,
                    onSave = { newJournal ->
                        viewModel.addJournal(newJournal)
                        isAdding.value = false
                    },
                    onCancel = { isAdding.value = false }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FieldLabelText("Time Out", Modifier.width(200.dp).align(Alignment.CenterVertically))
                FieldLabelText("Time In", Modifier.width(200.dp).align(Alignment.CenterVertically))
                FieldLabelText("Route", Modifier.width(200.dp).align(Alignment.CenterVertically))
                FieldLabelText("Number", Modifier.width(100.dp).align(Alignment.CenterVertically))

                if (isAdmin) {
                    EditDeleteButtons(false, {}, {})
                }
            }

            LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = Modifier.weight(1f)) {
                items(journals) { journal ->
                    JournalGridItem(
                        journal = journal,
                        routes = routes,
                        autos = autos,
                        onEditClick = {
                            editingJournal.value = journal
                            isEditing.value = true
                        },
                        onDeleteClick = { viewModel.deleteJournal(journal.id) }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isAdmin) {
                    AddButton(text = "Add New Journal Item") {
                        isAdding.value = true
                    }
                }

                DownloadReportButton(
                    list = journals,
                    context = LocalContext.current
                )
            }
        }

        actionError?.let {
            ErrorDialog(
                message = it,
                onDismiss = { viewModel.clearError() }
            )
        }
    }
}

