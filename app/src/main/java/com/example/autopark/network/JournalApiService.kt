package com.example.autopark.network

import com.example.autopark.dto.JournalDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JournalApiService {
    @GET("journal")
    suspend fun getJournals(): List<JournalDto>

    @POST("journal")
    suspend fun addJournal(@Body journalDto: JournalDto): JournalDto

    @PUT("journal/{id}")
    suspend fun updateJournal(@Path("id") id: Int, @Body journalDto: JournalDto): JournalDto

    @DELETE("journal/{id}")
    suspend fun deleteJournal(@Path("id") id: Int): Void
}
