package com.example.autopark.repository

import com.example.autopark.dto.UsersDto
import com.example.autopark.network.UsersApiService
import com.example.autopark.network.RetrofitInstance

class UsersRepository {
    private val apiService = RetrofitInstance.retrofit.create(UsersApiService::class.java)

    suspend fun getUsers(): List<UsersDto> = apiService.getUsers()
    suspend fun getUserById(id: Int): UsersDto? = apiService.getUserById(id)
    suspend fun addUser(userDto: UsersDto): UsersDto = apiService.addUser(userDto)
    suspend fun updateUser(id: Int, userDto: UsersDto): UsersDto = apiService.updateUser(id, userDto)
    suspend fun deleteUser(id: Int) = apiService.deleteUser(id)
}
