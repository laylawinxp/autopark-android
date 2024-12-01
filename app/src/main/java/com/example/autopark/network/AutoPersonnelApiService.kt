package com.example.autopark.network

import com.example.autopark.dto.AutoPersonnelDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AutoPersonnelApiService {
    @GET("personnel")
    suspend fun getAutoPersonnel(): List<AutoPersonnelDto>

    @POST("personnel")
    suspend fun addPersonnel(@Body personnelDto: AutoPersonnelDto): AutoPersonnelDto

    @PUT("personnel/{id}")
    suspend fun updatePersonnel(@Path("id") id: Int, @Body personnelDto: AutoPersonnelDto): AutoPersonnelDto

    @DELETE("personnel/{id}")
    suspend fun deletePersonnel(@Path("id") id: Int): Void
}
