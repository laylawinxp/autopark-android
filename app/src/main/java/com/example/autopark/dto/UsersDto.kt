package com.example.autopark.dto

data class UsersDto(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val isAdmin: Boolean
)
