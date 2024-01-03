package com.example.shopmatemobile.service

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class DateService {

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun parseDate(date: String): String? {
            val patterns = listOf("yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss")
            val formatterNeeded = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            for (pattern in patterns) {
                try {
                    val formatter = DateTimeFormatter.ofPattern(pattern)
                    val dateOfBirth = LocalDate.parse(date, formatter)
                    return dateOfBirth.format(formatterNeeded)
                } catch (e: DateTimeParseException) {
                    println("invalid date")
                }
            }
            return null
        }
    }
}