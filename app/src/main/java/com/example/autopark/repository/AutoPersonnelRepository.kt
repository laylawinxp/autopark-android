package com.example.autopark.repository

import com.example.autopark.dto.AutoPersonnelDto
import com.example.autopark.network.AutoPersonnelApiService
import com.example.autopark.network.RetrofitInstance

class AutoPersonnelRepository {
    private val apiService = RetrofitInstance.retrofit.create(AutoPersonnelApiService::class.java)

    suspend fun getAutoPersonnel(): List<AutoPersonnelDto> = apiService.getAutoPersonnel()
    suspend fun addPersonnel(personnelDto: AutoPersonnelDto): AutoPersonnelDto = apiService.addPersonnel(personnelDto)
    suspend fun updatePersonnel(id: Int, personnelDto: AutoPersonnelDto): AutoPersonnelDto = apiService.updatePersonnel(id, personnelDto)
    suspend fun deletePersonnel(id: Int) = apiService.deletePersonnel(id)
}
