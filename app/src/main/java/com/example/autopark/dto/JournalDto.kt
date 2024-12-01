package com.example.autopark.dto

data class JournalDto(
    val id: Int,
    val timeOut: String,
    val timeIn: String,
    val routeId: Int,
    val autoId: Int
)
