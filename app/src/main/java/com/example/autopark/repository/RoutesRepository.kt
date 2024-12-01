package com.example.autopark.repository

import com.example.autopark.dto.RouteCountDto
import com.example.autopark.dto.RoutesDto
import com.example.autopark.dto.ShortestTimeDto
import com.example.autopark.network.RetrofitInstance
import com.example.autopark.network.RoutesApiService

class RoutesRepository {
    private val apiService = RetrofitInstance.retrofit.create(RoutesApiService::class.java)

    suspend fun getRoutes(): List<RoutesDto> = apiService.getRoutes()
    suspend fun addRoute(routeDto: RoutesDto): RoutesDto = apiService.addRoute(routeDto)
    suspend fun updateRoute(id: Int, routeDto: RoutesDto): RoutesDto = apiService.updateRoute(id, routeDto)
    suspend fun deleteRoute(id: Int) = apiService.deleteRoute(id)
    suspend fun getCurrentlyInRoute(id: Int): RouteCountDto = apiService.getCurrentlyInRoute(id)
    suspend fun getShortestTime(id: Int): ShortestTimeDto = apiService.getShortestTime(id)
}
