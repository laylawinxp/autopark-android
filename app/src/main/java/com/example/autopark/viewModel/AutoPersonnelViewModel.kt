package com.example.autopark.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autopark.dto.AutoPersonnelDto
import com.example.autopark.repository.AutoPersonnelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AutoPersonnelViewModel : ViewModel() {
    private val repository = AutoPersonnelRepository()

    private val _autoPersonnel = MutableStateFlow<List<AutoPersonnelDto>>(emptyList())
    val autoPersonnel: StateFlow<List<AutoPersonnelDto>> get() = _autoPersonnel

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun getAutoPersonnel() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedPersonnel = repository.getAutoPersonnel()
                _autoPersonnel.value = fetchedPersonnel
            } catch (e: Exception) {
                Log.e("AutoPersonnelViewModel", "Error fetching auto personnel: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addPersonnel(personnelDto: AutoPersonnelDto) {
        viewModelScope.launch {
            try {
                val addedPersonnel = repository.addPersonnel(personnelDto)
                _autoPersonnel.value = _autoPersonnel.value.toMutableList().apply {
                    add(addedPersonnel)
                }
            } catch (e: Exception) {
                Log.e("AutoPersonnelViewModel", "Error adding personnel: ${e.message}", e)
                getAutoPersonnel()
            }
        }
    }

    fun updatePersonnel(id: Int, personnelDto: AutoPersonnelDto) {
        viewModelScope.launch {
            try {
                repository.updatePersonnel(id, personnelDto)
                _autoPersonnel.value = _autoPersonnel.value.map {
                    if (it.id == id) personnelDto else it
                }
            } catch (e: Exception) {
                Log.e("AutoPersonnelViewModel", "Error updating personnel: ${e.message}", e)
            }
        }
    }

    fun deletePersonnel(id: Int) {
        viewModelScope.launch {
            try {
                _autoPersonnel.value = _autoPersonnel.value.filterNot { it.id == id }
                repository.deletePersonnel(id)
            } catch (e: Exception) {
                Log.e("AutoPersonnelViewModel", "Error deleting personnel: ${e.message}", e)
            }
        }
    }
}
