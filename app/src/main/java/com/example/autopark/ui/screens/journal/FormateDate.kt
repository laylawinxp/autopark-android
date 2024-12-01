package com.example.autopark.ui.screens.journal

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(input: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    val date = inputFormat.parse(input)
    return outputFormat.format(date)
}

fun formatDateToSend(input: String): String {
    val inputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    val date = inputFormat.parse(input)
    return outputFormat.format(date)
}