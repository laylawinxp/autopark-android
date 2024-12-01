package com.example.autopark.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autopark.dto.AutoDto
import com.example.autopark.repository.AutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AutoViewModel : ViewModel() {
    private val repository = AutoRepository()

    private val _autos = MutableStateFlow<List<AutoDto>>(emptyList())
    val autos: StateFlow<List<AutoDto>> get() = _autos

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun getAutos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedAutos = repository.getAutos()
                _autos.value = fetchedAutos
            } catch (e: Exception) {
                Log.e("AutoViewModel", "Error fetching autos: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addAuto(autoDto: AutoDto) {
        viewModelScope.launch {
            try {
                repository.addAuto(autoDto)
                val newList = _autos.value.toMutableList().apply {
                    add(autoDto)
                }
                _autos.value = newList
            } catch (e: Exception) {
                Log.e("AutoViewModel", "Error adding auto: ${e.message}", e)
                getAutos()
            }
        }
    }

    fun updateAuto(id: Int, autoDto: AutoDto) {
        viewModelScope.launch {
            try {
                repository.updateAuto(id, autoDto)
                _autos.value = _autos.value.map {
                    if (it.id == id) autoDto else it
                }
            } catch (e: Exception) {
                Log.e("AutoViewModel", "Error updating auto: ${e.message}", e)
            }
        }
    }

    fun deleteAuto(id: Int) {
        viewModelScope.launch {
            try {
                _autos.value = _autos.value.filterNot { it.id == id }
                repository.deleteAuto(id)
            } catch (e: Exception) {
                Log.e("AutoViewModel", "Error deleting auto: ${e.message}", e)
            }
        }
    }
}
