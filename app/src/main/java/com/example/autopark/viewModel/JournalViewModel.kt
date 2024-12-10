package com.example.autopark.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autopark.dto.JournalDto
import com.example.autopark.repository.JournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JournalViewModel : ViewModel() {
    private val repository = JournalRepository()

    private val _journals = MutableStateFlow<List<JournalDto>>(emptyList())
    val journals: StateFlow<List<JournalDto>> get() = _journals

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _actionError = MutableStateFlow<String?>(null)
    val actionError: StateFlow<String?> get() = _actionError

    fun getJournals() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedJournals = repository.getJournals()
                _journals.value = fetchedJournals
            } catch (e: Exception) {
                Log.e("JournalViewModel", "Error fetching journals: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addJournal(journalDto: JournalDto) {
        viewModelScope.launch {
            try {
                val addedJournal = repository.addJournal(journalDto)
                _journals.value = _journals.value.toMutableList().apply {
                    add(addedJournal)
                }
                _actionError.value = null
            } catch (e: Exception) {
                Log.e(
                    "JournalViewModel",
                    "Error adding journal: ${e.message}", e
                )
                if (e.message?.contains("409") == true) {
                    _actionError.value =
                        "Unable to add journal item: Invalid data"
                }
            } finally {
                getJournals()
            }
        }
    }


    fun updateJournal(id: Int, journalDto: JournalDto) {
        viewModelScope.launch {
            try {
                repository.updateJournal(id, journalDto)
                _journals.value = _journals.value.map {
                    if (it.id == id) journalDto else it
                }
                _actionError.value = null
            } catch (e: Exception) {
                Log.e(
                    "JournalViewModel",
                    "Error updating journal: ${e.message} cause ${e.cause}  local ${e.localizedMessage}",
                    e
                )
                if (e.message?.contains("409") == true) {
                    _actionError.value =
                        "Unable to update journal: Arrival time cannot be earlier than departure time"
                }
            }
        }
    }


    fun deleteJournal(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteJournal(id)
                _journals.value = _journals.value.filterNot { it.id == id }
            } catch (e: Exception) {
                Log.e("JournalViewModel", "Error deleting journal: ${e.message}", e)
            }
        }
    }

    fun clearError() {
        _actionError.value = null
    }
}
