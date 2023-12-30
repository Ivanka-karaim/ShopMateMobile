package com.example.shopmatemobile.model

data class Review(var id: Int, var isVerified: Boolean, var productId: String, var text: String, var rating: Double, var userForReview: UserReview )
