package com.example.shopmatemobile.model

import java.sql.Date
import java.sql.Timestamp
import java.time.OffsetDateTime

data class Review(var id: Int, var isVerified: Boolean, var productId: String, var text: String, var rating: Double, var userForReview: UserReview, var date: String, var isThisUser:Boolean)
