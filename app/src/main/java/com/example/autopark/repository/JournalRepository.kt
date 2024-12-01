package com.example.autopark.repository

import com.example.autopark.dto.JournalDto
import com.example.autopark.network.JournalApiService
import com.example.autopark.network.RetrofitInstance

class JournalRepository {
    private val apiService = RetrofitInstance.retrofit.create(JournalApiService::class.java)

    suspend fun getJournals(): List<JournalDto> = apiService.getJournals()
    suspend fun addJournal(journalDto: JournalDto): JournalDto = apiService.addJournal(journalDto)
    suspend fun updateJournal(id: Int, journalDto: JournalDto): JournalDto = apiService.updateJournal(id, journalDto)
    suspend fun deleteJournal(id: Int) = apiService.deleteJournal(id)
}
