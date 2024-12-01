package com.example.autopark.repository

import com.example.autopark.dto.AutoDto
import com.example.autopark.network.AutoApiService
import com.example.autopark.network.RetrofitInstance

class AutoRepository {
    private val apiService = RetrofitInstance.retrofit.create(AutoApiService::class.java)

    suspend fun getAutos(): List<AutoDto> = apiService.getAutos()
    suspend fun addAuto(autoDto: AutoDto): AutoDto = apiService.addAuto(autoDto)
    suspend fun updateAuto(id: Int, autoDto: AutoDto): AutoDto = apiService.updateAuto(id, autoDto)
    suspend fun deleteAuto(id: Int) = apiService.deleteAuto(id)
}
