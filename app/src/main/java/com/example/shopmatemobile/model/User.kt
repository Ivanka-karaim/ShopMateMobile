package com.example.shopmatemobile.model

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val dateBirth: java.util.Date,
    val phoneNumber: String
)