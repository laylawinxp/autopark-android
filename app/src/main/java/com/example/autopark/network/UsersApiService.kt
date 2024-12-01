package com.example.autopark.network

import com.example.autopark.dto.UsersDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersApiService {
    @GET("users")
    suspend fun getUsers(): List<UsersDto>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UsersDto?

    @POST("users")
    suspend fun addUser(@Body userDto: UsersDto): UsersDto

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body userDto: UsersDto): UsersDto

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Void
}
