package com.example.autopark.ui.screens.journal

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
import com.example.autopark.dto.JournalDto
import com.example.autopark.ui.screens.AddButton
import com.example.autopark.viewModel.AutoViewModel
import com.example.autopark.viewModel.JournalViewModel
import com.example.autopark.viewModel.RoutesViewModel

@Composable
fun JournalScreen(
    viewModel: JournalViewModel,
    routeViewModel: RoutesViewModel,
    autoViewModel: AutoViewModel
) {
    val journals by viewModel.journals.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val routes by routeViewModel.routes.collectAsState(initial = emptyList())
    val autos by autoViewModel.autos.collectAsState(initial = emptyList())

    val isAdding = remember { mutableStateOf(false) }
    val isEditing = remember { mutableStateOf(false) }
    val editingJournal = remember { mutableStateOf<JournalDto?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getJournals()
        routeViewModel.getRoutes()
        autoViewModel.getAutos()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f)
            ) {
                items(journals) { journal ->
                    JournalGridItem(
                        journal = journal,
                        routes = routes,
                        autos =  autos,
                        onEditClick = {
                            editingJournal.value = journal
                            isEditing.value = true
                        },
                        onDeleteClick = {
                            viewModel.deleteJournal(journal.id)
                        }
                    )
                }
            }

            AddButton(text = "Add New Journal") {
                isAdding.value = true
            }
        }

        if (isEditing.value && editingJournal.value != null) {
            EditJournalForm(
                journal = JournalDto(editingJournal.value!!.id, formatDate(editingJournal.value!!.timeOut), formatDate(
                    editingJournal.value!!.timeIn), editingJournal.value!!.routeId, editingJournal.value!!.autoId),
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
                journal = JournalDto(id = 0, timeOut = "", timeIn = "", routeId = 0, autoId = 0),
                routeList = routes,
                autoList = autos,
                onSave = { newJournal ->
                    viewModel.addJournal(newJournal)
                    isAdding.value = false
                },
                onCancel = {
                    isAdding.value = false
                }
            )
        }
    }
}
