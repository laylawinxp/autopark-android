package com.example.autopark.network

import com.example.autopark.dto.AutoDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AutoApiService {
    @GET("autos")
    suspend fun getAutos(): List<AutoDto>

    @POST("autos")
    suspend fun addAuto(@Body autoDto: AutoDto): AutoDto

    @PUT("autos/{id}")
    suspend fun updateAuto(@Path("id") id: Int, @Body autoDto: AutoDto): AutoDto

    @DELETE("autos/{id}")
    suspend fun deleteAuto(@Path("id") id: Int): Void
}
