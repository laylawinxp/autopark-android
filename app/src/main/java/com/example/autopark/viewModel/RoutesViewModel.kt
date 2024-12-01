package com.example.autopark.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autopark.dto.RoutesDto
import com.example.autopark.repository.RoutesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoutesViewModel : ViewModel() {
    private val repository = RoutesRepository()

    private val _routes = MutableStateFlow<List<RoutesDto>>(emptyList())
    val routes: StateFlow<List<RoutesDto>> get() = _routes

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _currentRouteCounts = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val currentRouteCounts: StateFlow<Map<Int, Int>> get() = _currentRouteCounts

    private val _timeAndCar = MutableStateFlow<Map<Int, Pair<String, String>>>(emptyMap())
    val timeAndCar: StateFlow<Map<Int, Pair<String, String>>> get() = _timeAndCar

    fun getRoutes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedRoutes = repository.getRoutes()
                _routes.value = fetchedRoutes

                fetchedRoutes.forEach { route ->
                    getCurrentlyInRoute(route.id)
                }
                fetchedRoutes.forEach { route ->
                    getMinTimeAndBestCar(route.id)
                }
            } catch (e: Exception) {
                Log.e("RoutesViewModel", "Error fetching routes: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCurrentlyInRoute(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentlyInRoute(id)
                val count = response.count

                _currentRouteCounts.value = _currentRouteCounts.value.toMutableMap().apply {
                    put(id, count)
                }

            } catch (e: Exception) {
                Log.e("RoutesViewModel", "Error fetching currently in route count: ${e.message}", e)
            }
        }
    }

    fun getMinTimeAndBestCar(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getShortestTime(id)
                val timeSec = response.shortestTimeSec
                val carId = response.fastestCarId

                _timeAndCar.value = _timeAndCar.value.toMutableMap().apply {
                    put(id, Pair(timeSec, carId))
                }

            } catch (e: Exception) {
                Log.e("RoutesViewModel", "Error fetching fastest car and min time: ${e.message}", e)
            }
        }
    }

    fun addRoute(routeDto: RoutesDto) {
        viewModelScope.launch {
            try {
                Log.d("RoutesViewModel", "Adding route: $routeDto")

                val addedRoute = repository.addRoute(routeDto)

                _routes.value = _routes.value.toMutableList().apply {
                    add(addedRoute)
                }

                Log.d("RoutesViewModel", "Route added successfully: $addedRoute")
            } catch (e: Exception) {
                Log.e("RoutesViewModel", "Error adding route: ${e.message}", e)
                getRoutes()
            }
        }
    }

    fun updateRoute(id: Int, routeDto: RoutesDto) {
        viewModelScope.launch {
            try {
                repository.updateRoute(id, routeDto)
                _routes.value = _routes.value.map {
                    if (it.id == id) routeDto else it
                }
            } catch (e: Exception) {
                Log.e("RoutesViewModel", "Error updating route: ${e.message}", e)
            }
        }
    }

    fun deleteRoute(id: Int) {
        viewModelScope.launch {
            try {
                _routes.value = _routes.value.filterNot { it.id == id }
                repository.deleteRoute(id)
            } catch (e: Exception) {
                Log.e("RoutesViewModel", "Error deleting route: ${e.message}", e)
            }
        }
    }
}
