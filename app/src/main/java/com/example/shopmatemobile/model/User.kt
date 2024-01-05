package com.example.shopmatemobile.model

import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val dateBirth: String,
    val phoneNumber: String
)