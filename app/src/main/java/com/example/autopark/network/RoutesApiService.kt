package com.example.autopark.network

import com.example.autopark.dto.RouteCountDto
import com.example.autopark.dto.RoutesDto
import com.example.autopark.dto.ShortestTimeDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoutesApiService {
    @GET("routes")
    suspend fun getRoutes(): List<RoutesDto>

    @POST("routes")
    suspend fun addRoute(@Body routeDto: RoutesDto): RoutesDto

    @PUT("routes/{id}")
    suspend fun updateRoute(@Path("id") id: Int, @Body routeDto: RoutesDto): RoutesDto

    @DELETE("routes/{id}")
    suspend fun deleteRoute(@Path("id") id: Int): Void

    @GET("routes/{id}/currently-in-route")
    suspend fun getCurrentlyInRoute(@Path("id") id: Int): RouteCountDto

    @GET("routes/{id}/shortest-time")
    suspend fun getShortestTime(@Path("id") id: Int): ShortestTimeDto
}
